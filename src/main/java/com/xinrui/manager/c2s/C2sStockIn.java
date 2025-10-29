package com.xinrui.manager.c2s;

import lombok.Data;
import java.util.List;

@Data
public class C2sStockIn {
    /**
     * 供应商ID
     */
    private Integer supplierId;
    /**
     * 进货商品项列表
     */
    private List<C2sStockInItem> items;
    /**
     * 进货备注
     */
    private String remark;
}