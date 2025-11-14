package com.xinrui.service;

import com.xinrui.entity.Goods;
import com.xinrui.manager.c2s.*;
import com.xinrui.manager.s2c.S2cGoodsManager;
import com.xinrui.manager.s2c.S2cSettlement;
import com.xinrui.manager.s2c.S2cStockIn;

import java.util.List;

public interface IGoodsManagerSVC {

    /**
     * 获取商品
     * @param c2sGoodsQuery
     * @return
     */
    List<S2cGoodsManager> getGoods(C2sGoodsQuery c2sGoodsQuery);

    /**
     * 商品的增加/禁用/修改
     * @param goods
     */
    void publishGoods(Goods goods);

    /**
     * 商品的删除
     * @param goods
     */
    void deleteGoods(Goods goods);

    /**
     * 商品拆包 - 获取子包装
     * @param goods
     * @return
     */
    S2cGoodsManager unboxToChildPackage(Goods goods);

    /**
     * 商品拆包 - 获取母包装
     * @param goods
     * @return
     */
    S2cGoodsManager unboxToMotherPackage(Goods goods);

    /**
     * 商品结算
     * @param c2sSettlement 结算请求
     * @return 结算结果
     */
    S2cSettlement settlement(C2sSettlement c2sSettlement);

    /**
     * 商品进货
     * @param c2sStockIn 进货请求
     * @return 进货结果
     */
    S2cStockIn stockIn(C2sStockIn c2sStockIn);
}
