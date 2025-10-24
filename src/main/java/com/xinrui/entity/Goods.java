package com.xinrui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "goods")
public class Goods {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 是否启用
     */
    private Integer isuse;
    /**
     * 分类id
     */
    private Integer typeId;
    /**
     * 单位
     */
    private String unit;
    /**
     * 条码
     */
    private String bar;
    /**
     * 名称
     */
    private String name;
    /**
     * 存货量
     */
    private Integer stock;
    /**
     * 进价
     */
    private Float purPrice;
    /**
     * 售价
     */
    private Float price;
    /**
     * 利润
     */
    private Float profit;
    /**
     * 生产日期
     */
    private String proDate;
    /**
     * 截止日期
     */
    private String expDate;
    /**
     * 入库日期
     */
    private String putDate;
    /**
     * 保质期
     */
    private Integer baoZhiQi;
    /**
     * 转换码
     */
    private String zhuanHuanMa;
    /**
     * 转换比
     */
    private Integer zhuanHuanBi;
}
