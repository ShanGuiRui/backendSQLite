package com.xinrui.ruler;

import com.xinrui.entity.Supplier;
import com.xinrui.manager.c2s.C2sSupplier;
import com.xinrui.manager.c2s.C2sSupplierQuery;
import com.xinrui.manager.s2c.S2cSupplier;
import com.xinrui.service.ISupplierManagerSVC;
import com.xinrui.utils.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierManagerRuler {

    @Autowired
    private ISupplierManagerSVC supplierSVC;

    /**
     * 获取供应商列表
     */
    @PostMapping("/getSuppliers")
    public ApiResult getSuppliers(@RequestBody C2sSupplierQuery query) {
        if (query == null) {
            query = new C2sSupplierQuery();
        }
        if (query.getPage() == null) {
            query.setPage(new com.xinrui.utils.Page(null, com.xinrui.utils.Page.DEFAULT_PAGE_SIZE));
        }

        List<S2cSupplier> result = supplierSVC.getSuppliers(query);
        return ApiResult.success()
                .setData(result)
                .set("hasMore", result.size() >= query.getPage().getPageSize());
    }

    /**
     * 增删改供应商
     */
    @PostMapping("/publishSupplier")
    public ApiResult publishSupplier(@RequestBody C2sSupplier c2sSupplier) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(c2sSupplier, supplier);
        supplierSVC.publishSupplier(supplier);
        return ApiResult.success().setData(0);
    }

    /**
     * 删除供应商
     */
    @PostMapping("/deleteSupplier")
    public ApiResult deleteSupplier(@RequestParam Integer id) {
        supplierSVC.deleteSupplier(id);
        return ApiResult.success().setData(0);
    }
}