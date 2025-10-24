package com.xinrui.ruler;

import com.xinrui.utils.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestRuler {
    /**
     * 测试接口
     * @return
     */
    @GetMapping
    public ApiResult test() {
        return ApiResult.success()
                .setData("ok");
    }
}
