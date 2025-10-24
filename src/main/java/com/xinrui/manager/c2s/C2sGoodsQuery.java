package com.xinrui.manager.c2s;

import com.xinrui.utils.Page;
import lombok.Data;

@Data
public class C2sGoodsQuery {
    // 分页信息
    private Page page;

    // 过滤条件
    private Integer isuse;
    private String keyword;
    private Integer typeId;

    // 排序字段
    private String sortField = "id";
    private boolean sortAsc = false;
}
