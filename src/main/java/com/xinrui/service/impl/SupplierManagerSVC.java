package com.xinrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xinrui.entity.Supplier;
import com.xinrui.manager.c2s.C2sSupplierQuery;
import com.xinrui.manager.s2c.S2cSupplier;
import com.xinrui.mapper.SupplierMapper;
import com.xinrui.service.ISupplierManagerSVC;
import com.xinrui.utils.Page;
import org.springframework.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierManagerSVC implements ISupplierManagerSVC {

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 获取供应商列表
     * @param query 查询条件
     * @return
     */
    @Override
    public List<S2cSupplier> getSuppliers(C2sSupplierQuery query) {
        // 初始化查询条件
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();

        // 分页处理
        if (query.getPage() == null) {
            query.setPage(new Page(null, Page.DEFAULT_PAGE_SIZE));
        }
        Page page = query.getPage();
        if (!page.isFirstLoad() && page.getLastId() != null) {
            queryWrapper.lt("id", page.getLastId());
        }
        queryWrapper.last("LIMIT " + page.getPageSize());

        // 条件过滤
        // 按启用状态过滤
        if (query.getIsuse() != null) {
            queryWrapper.eq("isuse", query.getIsuse());
        }
        // 整合keyword模糊搜索
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            String keyword = query.getKeyword();
            queryWrapper.and(wrapper -> wrapper
                    .like("name", keyword)
                    .or()
                    .like("contact_person", keyword)
                    .or()
                    .like("phone", keyword)
            );
        }

        // 按ID倒序排序
        queryWrapper.orderByDesc("id");

        // 执行查询并转换为响应DTO
        List<Supplier> suppliers = supplierMapper.selectList(queryWrapper);
        return suppliers.stream()
                .map(supplier -> {
                    S2cSupplier dto = new S2cSupplier();
                    BeanUtils.copyProperties(supplier, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 供应商的增删改
     * @param supplier 供应商信息
     */
    @Override
    public void publishSupplier(Supplier supplier) {
        if (StringUtils.isEmpty(supplier.getId())) {
            // 新增
            supplierMapper.insert(supplier);
            return;
        }
        // 修改
        supplierMapper.updateById(supplier);
    }

    @Override
    public void deleteSupplier(Integer id) {
        supplierMapper.deleteById(id);
    }
}