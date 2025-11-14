package com.xinrui.service;

import com.xinrui.entity.GoodsType;
import com.xinrui.manager.s2c.S2cGoodsTypeManager;

import java.util.List;

public interface IGoodsTypeManagerSVC {

    /**
     * 获取商品分类
     * @return
     */
    List<S2cGoodsTypeManager> getGoodsType(GoodsType goodsType);

    /**
     * 分类的增加/禁用/修改
     * @param goodsType
     */
    void publishGoodsType(GoodsType goodsType);

    /**
     * 删除分类
     * @param goodsType
     */
    void deleteGoodsType(GoodsType goodsType);
}
