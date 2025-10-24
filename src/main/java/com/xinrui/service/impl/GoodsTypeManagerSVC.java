package com.xinrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xinrui.entity.GoodsType;
import com.xinrui.manager.c2s.C2sGoodsTypeManager;
import com.xinrui.manager.s2c.S2cGoodsTypeManager;
import com.xinrui.mapper.GoodsTypeMapper;
import com.xinrui.service.IGoodsTypeManagerSVC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsTypeManagerSVC implements IGoodsTypeManagerSVC {
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    /**
     * 获取商品分类
     * @return
     */
    @Override
    public List<S2cGoodsTypeManager> getGoodsType(GoodsType goodsType) {
        QueryWrapper<GoodsType> queryWrapper = new QueryWrapper<>();

        // 可选状态过滤
        if (goodsType.getIsuse() != null) {
            queryWrapper.eq("isuse", goodsType.getIsuse());
        }

        List<GoodsType> goodsTypeList = goodsTypeMapper.selectList(queryWrapper);

        List<S2cGoodsTypeManager> s2cGoodsTypeManagerList = goodsTypeList.stream().map(GoodsType ->{
            S2cGoodsTypeManager s2cGoodsTypeManager = new S2cGoodsTypeManager();
            BeanUtils.copyProperties(GoodsType,s2cGoodsTypeManager);
            return s2cGoodsTypeManager;
        }).collect(Collectors.toList());

        return s2cGoodsTypeManagerList;
    }

    /**
     * 分类的增删改
     * @param goodsType
     */
    @Override
    public void publishGoodsType(GoodsType goodsType) {
        if(StringUtils.isEmpty(goodsType.getTypeId())){
            goodsTypeMapper.insert(goodsType);
            return;
        }
        goodsTypeMapper.updateById(goodsType);
    }

}
