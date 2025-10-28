package com.xinrui.manager.c2s;

import com.xinrui.utils.Page;
import lombok.Data;

@Data
public class C2sSupplierQuery {
    // 分页信息
    private Page page;

    // 过滤条件
    private Integer isuse; // 按启用状态过滤
    private String keyword; // 模糊搜索（名称/联系人/电话）
}