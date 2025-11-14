package com.xinrui.ruler;

import com.xinrui.entity.Goods;
import com.xinrui.manager.c2s.C2sGoodsManager;
import com.xinrui.manager.c2s.C2sGoodsQuery;
import com.xinrui.manager.c2s.C2sSettlement;
import com.xinrui.manager.c2s.C2sStockIn;
import com.xinrui.manager.s2c.S2cGoodsManager;
import com.xinrui.manager.s2c.S2cSettlement;
import com.xinrui.manager.s2c.S2cStockIn;
import com.xinrui.service.IGoodsManagerSVC;
import com.xinrui.utils.ApiResult;
import com.xinrui.utils.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsManagerRuler {
    @Autowired
    private IGoodsManagerSVC goodsSVC;

    /**
     * 获取商品
     * @param c2sGoodsQuery
     * @return
     */
    @PostMapping("/getGoods")
    public ApiResult getGoods(@RequestBody C2sGoodsQuery c2sGoodsQuery) {
        // 参数校验
        if (c2sGoodsQuery == null) {
            c2sGoodsQuery = new C2sGoodsQuery();
        }
        if (c2sGoodsQuery.getPage() == null) {
            c2sGoodsQuery.setPage(new Page(null, Page.DEFAULT_PAGE_SIZE));
        }

        // 执行查询
        List<S2cGoodsManager> result = goodsSVC.getGoods(c2sGoodsQuery);

        // 构建响应
        return ApiResult.success()
                .setData(result)
                .set("hasMore", result.size() >= c2sGoodsQuery.getPage().getPageSize());
    }

    /**
     * 商品的增加/禁用/修改
     * @param c2sGoodsManager
     * @return
     */
    @PostMapping("/publishGoods")
    public ApiResult publishGoods(@RequestBody C2sGoodsManager c2sGoodsManager){
        Goods goods = new Goods();
        BeanUtils.copyProperties(c2sGoodsManager, goods);
        goodsSVC.publishGoods(goods);
        return ApiResult.success()
                .setData(0);
    }

    /**
     * 商品的删除
     * @param c2sGoodsManager
     * @return
     */
    @PostMapping("/deleteGoods")
    public ApiResult deleteGoods(@RequestBody C2sGoodsManager c2sGoodsManager){
        Goods goods = new Goods();
        BeanUtils.copyProperties(c2sGoodsManager, goods);
        goodsSVC.deleteGoods(goods);
        return ApiResult.success()
                .setData(0);
    }

    /**
     * 商品拆包 - 获取子包装
     * @param c2sGoodsManager
     * @return
     */
    @PostMapping("/unboxToChildPackage")
    public ApiResult unboxToChildPackage(@RequestBody C2sGoodsManager c2sGoodsManager) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(c2sGoodsManager, goods);
        goodsSVC.unboxToChildPackage(goods);
        return ApiResult.success()
                .setData(0);
    }

    /**
     * 商品拆包 - 获取母包装
     * @param c2sGoodsManager
     * @return
     */
    @PostMapping("/unboxToMotherPackage")
    public ApiResult unboxToMotherPackage(@RequestBody C2sGoodsManager c2sGoodsManager) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(c2sGoodsManager, goods);
        goodsSVC.unboxToMotherPackage(goods);
        return ApiResult.success()
                .setData(0);
    }

    /**
     * 商品结算
     * @param c2sSettlement
     * @return
     */
    @PostMapping("/settlement")
    public ApiResult settlement(@RequestBody C2sSettlement c2sSettlement) {
        S2cSettlement result = goodsSVC.settlement(c2sSettlement);
        return ApiResult.success()
                .setData(result)
                .set("totalAmount", result.getTotalAmount());
    }

    /**
     * 商品进货
     * @param c2sStockIn 进货请求
     * @return 进货结果
     */
    @PostMapping("/stockIn")
    public ApiResult stockIn(@RequestBody C2sStockIn c2sStockIn) {
        if (c2sStockIn == null || c2sStockIn.getItems() == null || c2sStockIn.getItems().isEmpty()) {
            return ApiResult.error("进货商品列表不能为空");
        }
        S2cStockIn result = goodsSVC.stockIn(c2sStockIn);
        return ApiResult.success()
                .setData(result)
                .set("totalAmount", result.getTotalAmount());
    }
}
