package com.xinrui.manager.c2s;

import lombok.Data;

@Data
public class C2sSettlementItem {
    /**
     * 结算商品
     */
    // 商品ID
    private Integer goodsId;
    // 购买数量
    private Integer quantity;
}