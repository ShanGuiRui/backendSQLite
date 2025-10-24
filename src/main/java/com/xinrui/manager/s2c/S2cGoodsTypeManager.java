package com.xinrui.manager.s2c;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class S2cGoodsTypeManager {
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
