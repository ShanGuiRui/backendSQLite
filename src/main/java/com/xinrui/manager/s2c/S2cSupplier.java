// src/main/java/com/xinrui/manager/s2c/S2cSupplier.java
package com.xinrui.manager.s2c;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class S2cSupplier {
    /**
     * 供应商ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 供应商名称
     */
    private String name;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否启用（1：启用，0：禁用）
     */
    private Integer isuse;

    /**
     * 备注
     */
    private String remark;
}