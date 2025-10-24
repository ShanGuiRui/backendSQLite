package com.xinrui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "goods_type")
public class GoodsType {
    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Integer typeId;
    /**
     * 是否启用
     */
    private Integer isuse;
    /**
     * 分类名
     */
    private String typeName;
    /**
     * 排序
     */
    private Integer sort;
}
