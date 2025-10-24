package com.xinrui.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResult {

    private Boolean success;
    private Integer code;
    private String message;
    private Object data;
    private Map<String, Object> extra = new HashMap<>();

    public ApiResult setData(Object data) {
        this.data = data;
        return this;
    }

    public ApiResult set(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }

    public static ApiResult success() {
        ApiResult result = new ApiResult();
        result.setSuccess(true);
        result.setCode(200);
        return result;
    }

    public static ApiResult error(String message) {
        ApiResult result = new ApiResult();
        result.setSuccess(false);
        result.setCode(400);
        result.setMessage(message);
        return result;
    }

    public static ApiResult error(String message, Integer code) {
        ApiResult result = new ApiResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
