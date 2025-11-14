package com.xinrui.ruler;

import com.xinrui.entity.GoodsType;
import com.xinrui.manager.c2s.C2sGoodsTypeManager;
import com.xinrui.manager.s2c.S2cGoodsTypeManager;
import com.xinrui.service.IGoodsTypeManagerSVC;
import com.xinrui.utils.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/goodsType")
public class GoodsTypeManagerRuler {
    @Autowired
    private IGoodsTypeManagerSVC goodsTypeSVC;

    /**
     * 获取商品分类
     * @param c2sGoodsTypeManager
     * @return
     */
    @PostMapping("/getGoodsType")
    public ApiResult getGoodsType(@RequestBody C2sGoodsTypeManager c2sGoodsTypeManager) {
        GoodsType goodsType = new GoodsType();
        BeanUtils.copyProperties(c2sGoodsTypeManager, goodsType);
        List<S2cGoodsTypeManager> result = goodsTypeSVC.getGoodsType(goodsType);
        return ApiResult.success().
                setData(result);
    }

    /**
     * 分类的增加/禁用/修改
     * @param c2sGoodsTypeManager
     * @return
     */
    @PostMapping("/publishGoodsType")
    public ApiResult publishGoodsType(@RequestBody C2sGoodsTypeManager c2sGoodsTypeManager){
        GoodsType goodsType = new GoodsType();
        BeanUtils.copyProperties(c2sGoodsTypeManager, goodsType);
        goodsTypeSVC.publishGoodsType(goodsType);
        return ApiResult.success()
                .setData(0);
    }

    /**
     * 删除分类
     * @param c2sGoodsTypeManager
     * @return
     */
    @PostMapping("/deleteGoodsType")
    public ApiResult deleteGoodsType(@RequestBody C2sGoodsTypeManager c2sGoodsTypeManager){
        GoodsType goodsType = new GoodsType();
        BeanUtils.copyProperties(c2sGoodsTypeManager, goodsType);
        goodsTypeSVC.deleteGoodsType(goodsType);
        return ApiResult.success()
                .setData(0);
    }
}
