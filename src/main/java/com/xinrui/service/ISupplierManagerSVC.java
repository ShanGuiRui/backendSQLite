package com.xinrui.service;

import com.xinrui.entity.Supplier;
import com.xinrui.manager.c2s.C2sSupplierQuery;
import com.xinrui.manager.s2c.S2cSupplier;

import java.util.List;

public interface ISupplierManagerSVC {

    /**
     * 获取供应商列表（支持分页、条件过滤）
     * @param query 查询条件
     * @return 供应商列表
     */
    List<S2cSupplier> getSuppliers(C2sSupplierQuery query);

    /**
     * 新增/修改供应商
     * @param supplier 供应商信息
     */
    void publishSupplier(Supplier supplier);

    /**
     * 删除供应商（逻辑删除或物理删除，此处实现物理删除）
     * @param id 供应商ID
     */
    void deleteSupplier(Integer id);
}