package com.xinrui.service;

import com.xinrui.entity.GoodsType;
import com.xinrui.manager.c2s.C2sGoodsTypeManager;
import com.xinrui.manager.s2c.S2cGoodsTypeManager;

import java.util.List;

public interface IGoodsTypeManagerSVC {

    /**
     * 获取商品分类
     * @return
     */
    List<S2cGoodsTypeManager> getGoodsType(GoodsType goodsType);

    /**
     * 分类的增删改
     * @param goodsType
     */
    void publishGoodsType(GoodsType goodsType);
}
