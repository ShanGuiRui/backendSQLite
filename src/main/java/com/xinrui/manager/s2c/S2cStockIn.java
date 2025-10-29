package com.xinrui.manager.s2c;

import lombok.Data;

@Data
public class S2cStockIn {
    // 总进货金额
    private Float totalAmount;
    // 进货结果信息
    private String message;
}