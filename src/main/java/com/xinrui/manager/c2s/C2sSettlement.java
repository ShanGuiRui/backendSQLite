package com.xinrui.manager.c2s;

import lombok.Data;
import java.util.List;

@Data
public class C2sSettlement {
    /**
     * 结算商品项列表
     */
    private List<C2sSettlementItem> items;
}
