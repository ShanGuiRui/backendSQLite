package com.xinrui.utils;

import lombok.Data;

@Data
public class Page {
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页最后一条记录的ID（用于瀑布流）
     */
    private Long lastId;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 是否首次加载
     */
    private boolean firstLoad = true;

    public Page(Long lastId, int pageSize) {
        this.lastId = lastId;
        this.pageSize = pageSize == 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

}
