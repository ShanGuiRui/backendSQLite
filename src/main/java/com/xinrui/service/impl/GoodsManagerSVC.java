package com.xinrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xinrui.entity.Goods;
import com.xinrui.entity.GoodsType;
import com.xinrui.manager.c2s.*;
import com.xinrui.manager.s2c.S2cGoodsManager;
import com.xinrui.manager.s2c.S2cSettlement;
import com.xinrui.mapper.GoodsMapper;
import com.xinrui.mapper.GoodsTypeMapper;
import com.xinrui.service.IGoodsManagerSVC;
import com.xinrui.utils.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsManagerSVC implements IGoodsManagerSVC {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    /**
     * 获取商品
     * @param c2sGoodsQuery
     * @return
     */
    @Override
    public List<S2cGoodsManager> getGoods(C2sGoodsQuery c2sGoodsQuery) {
        // 1. 初始化查询条件
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();

        // 2. 设置默认分页
        if (c2sGoodsQuery.getPage() == null) {
            c2sGoodsQuery.setPage(new Page(null, Page.DEFAULT_PAGE_SIZE));
        }

        // 3. 添加过滤条件
        // isuse状态过滤
        if (c2sGoodsQuery.getIsuse() != null) {
            queryWrapper.eq("isuse", c2sGoodsQuery.getIsuse());
        }
        // typeId分类过滤
        if (c2sGoodsQuery.getTypeId() != null) {
            queryWrapper.eq("type_id", c2sGoodsQuery.getTypeId());
        }
        // keyword整合模糊搜索
        if (c2sGoodsQuery.getKeyword() != null){
            String keyword = c2sGoodsQuery.getKeyword();
            queryWrapper.and(wrapper -> wrapper
                            .like("bar", keyword)
                            .or()
                            .like("name", keyword)
                    )
                    .orderByDesc("id");
        }

        // 4. 添加排序
        queryWrapper.orderBy(true, c2sGoodsQuery.isSortAsc(), c2sGoodsQuery.getSortField());

        // 5. 添加分页
        Page page = c2sGoodsQuery.getPage();
        if (!page.isFirstLoad() && page.getLastId() != null) {
            queryWrapper.lt("id", page.getLastId());
        }
        queryWrapper.last("LIMIT " + page.getPageSize());

        // 6. 执行查询
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
        Map<Integer, String> typeMap = getTypeNameMap(goodsList);

        // 7. 转换DTO
        return goodsList.stream()
                .map(g -> {
                    S2cGoodsManager dto = new S2cGoodsManager();
                    BeanUtils.copyProperties(g, dto);
                    dto.setTypeName(typeMap.getOrDefault(g.getTypeId(), "无分类"));
                    return dto;
                })
                .collect(Collectors.toList());
    }



    /**
     * 商品的增删改
     * @param goods
     */
    @Override
    @Transactional
    public void publishGoods(Goods goods) {
        // 自动计算利润
        if (goods.getPurPrice() != null && goods.getPrice() != null) {
            goods.setProfit(goods.getPrice() - goods.getPurPrice());
        }

        // 保存数据
        if (goods.getId() == null) {
            goodsMapper.insert(goods);
        } else {
            goodsMapper.updateById(goods);
        }
    }

    /**
     * 商品拆包 - 获取子包装
     * @param goods
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public S2cGoodsManager unboxToChildPackage(Goods goods) {
        // 1. 校验必要字段
        if (goods.getId() == null) {
            throw new RuntimeException("必须传入商品ID");
        }

        // 2. 查询商品A
        Goods goodsA = goodsMapper.selectById(goods.getId());
        if (goodsA == null) {
            throw new RuntimeException("未找到该商品");
        }

        // 3. 查询商品B（通过商品A的转换码）
        Goods goodsB = goodsMapper.selectOne(new QueryWrapper<Goods>()
                .eq("bar", goodsA.getZhuanHuanMa()));  // 直接从商品A取转换码
        if (goodsB == null) {
            throw new RuntimeException("未找到关联商品");
        }

        // 4. 调用拆包实现方法
        return unboxGoods(goodsA, goodsB);
    }

    /**
     * 商品拆包 - 获取母包装
     * @param goods
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public S2cGoodsManager unboxToMotherPackage(Goods goods) {
        // 1. 校验必要字段
        if (goods.getId() == null) {
            throw new RuntimeException("必须传入商品ID");
        }

        // 2. 查询商品B
        Goods goodsB = goodsMapper.selectById(goods.getId());
        if (goodsB == null) {
            throw new RuntimeException("未找到该商品");
        }

        // 3. 查询商品A（通过商品B的条码）
        Goods goodsA = goodsMapper.selectOne(new QueryWrapper<Goods>()
                .eq("zhuan_huan_ma", goodsB.getBar()));  // 直接从商品B取条码
        if (goodsA == null) {
            throw new RuntimeException("未找到关联商品");
        }

        // 4. 调用拆包实现方法
        return unboxGoods(goodsA, goodsB);
    }

    /**
     * 商品结算
     * @param c2sSettlement 结算请求
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public S2cSettlement settlement(C2sSettlement c2sSettlement) {
        float totalAmount = 0f;
        List<Goods> goodsList = new ArrayList<>();
        List<Goods> zeroStockGoodsBarcodes = new ArrayList<>(); // 记录库存变为0的商品条码

        // 1. 校验并锁定库存
        for (C2sSettlementItem item : c2sSettlement.getItems()) {
            Goods goods = goodsMapper.selectById(item.getGoodsId());
            if (goods == null) {
                throw new RuntimeException("商品不存在: " + item.getGoodsId());
            }
            if (goods.getStock() < item.getQuantity()) {
                throw new RuntimeException("库存不足: " + goods.getName());
            }
            goodsList.add(goods);
        }

        // 2. 扣减库存并计算总价
        for (int i = 0; i < c2sSettlement.getItems().size(); i++) {
            C2sSettlementItem item = c2sSettlement.getItems().get(i);
            Goods goods = goodsList.get(i);

            // 记录原始库存用于后续检查
            int originalStock = goods.getStock();

            // 扣减库存
            goods.setStock(goods.getStock() - item.getQuantity());
            goodsMapper.updateById(goods);

            // 检查是否需要自动拆包（库存变为0且商品有条码）
            if (originalStock > 0 && goods.getStock() == 0 &&
                goods.getBar() != null && !goods.getBar().isEmpty()) {
                zeroStockGoodsBarcodes.add(goods);
            }

            // 计算金额
            totalAmount += goods.getPrice() * item.getQuantity();
        }

        // 3. 自动拆包处理
        List<String> autoUnboxResults = new ArrayList<>();
        for (Goods goodsB : zeroStockGoodsBarcodes) {
            try {
                S2cGoodsManager result = unboxToMotherPackage(goodsB);
                autoUnboxResults.add("商品条码 " + goodsB.getBar() + " 自动拆包成功");
            } catch (Exception e) {
                // 自动拆包失败不影响主流程
            }
        }

        // 4. 构建响应
        S2cSettlement response = new S2cSettlement();
        response.setTotalAmount(totalAmount);

        // 添加结果信息
        String message = "结算成功，共" + c2sSettlement.getItems().size() + "件商品";
        if (!autoUnboxResults.isEmpty()) {
            message += "，自动拆包" + autoUnboxResults.size() + "次";
        }
        response.setMessage(message);

        return response;
    }

    //以下为无interface的私有方法，共实现层调用
    /**
     * 根据商品列表获取分类名称映射表
     * @param goodsList 商品列表
     * @return 分类ID到分类名称的映射Map
     */
    private Map<Integer, String> getTypeNameMap(List<Goods> goodsList) {
        // 收集所有涉及的类型ID
        List<Integer> typeIds = goodsList.stream()
                .map(goods -> goods.getTypeId())
                .filter(Objects::nonNull)  // 过滤掉null的typeId
                .distinct()
                .collect(Collectors.toList());

        if (typeIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 批量查询分类信息
        List<GoodsType> goodsTypes = goodsTypeMapper.selectBatchIds(typeIds);

        // 构建类型ID到类型名的映射
        return goodsTypes.stream()
                .collect(Collectors.toMap(
                        goodsType -> goodsType.getTypeId(),  // Integer类型
                        goodsType -> goodsType.getTypeName(),
                        (oldValue, newValue) -> oldValue));  // 如果有重复key，保留旧值
    }

    /**
     * 商品拆包的实现方法
     * @param goodsA
     * @param goodsB
     * @return
     */
    private S2cGoodsManager unboxGoods(Goods goodsA,Goods goodsB) {
        // 1. 校验库存
        if (goodsA.getStock() < 1) {
            throw new RuntimeException("商品库存不足");
        }

        // 2. 更新库存
        int convertRatio = goodsA.getZhuanHuanBi();  // 读取整数转换比
        goodsA.setStock(goodsA.getStock() - 1);      // A商品减1
        goodsB.setStock(goodsB.getStock() + convertRatio); // B商品加转换比

        goodsMapper.updateById(goodsA);
        goodsMapper.updateById(goodsB);

        // 3. 返回商品B信息
        S2cGoodsManager response = new S2cGoodsManager();
        BeanUtils.copyProperties(goodsB, response);
        return response;
    }
}
