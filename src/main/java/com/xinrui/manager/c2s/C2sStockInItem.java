package com.xinrui.manager.c2s;

import lombok.Data;

@Data
public class C2sStockInItem {
    /**
     * 商品ID
     */
    private Integer goodsId;
    /**
     * 进货数量
     */
    private Integer quantity;
    /**
     * 进货单价
     */
    private Float purPrice;
}