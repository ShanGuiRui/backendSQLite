package com.xinrui.manager.s2c;

import lombok.Data;

@Data
public class S2cSettlement {
    // 结算总金额
    private Float totalAmount;
    // 结算结果信息
    private String message;
}