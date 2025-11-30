package com.ai.cockpit.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一API响应结果
 */
@Data
@NoArgsConstructor
public class ApiResult<T> implements Serializable {
    
    /** 状态码 */
    private Integer code;
    
    /** 消息 */
    private String message;
    
    /** 数据 */
    private T data;
    
    /** 时间戳 */
    private Long timestamp;
    
    public ApiResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(200, "操作成功", null);
    }
    
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }
    
    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message, null);
    }
    
    public static <T> ApiResult<T> error(Integer code, String message) {
        return new ApiResult<>(code, message, null);
    }
    
    public static <T> ApiResult<T> error(Integer code, String message, T data) {
        return new ApiResult<>(code, message, data);
    }
    
    /**
     * 业务错误响应
     */
    public static <T> ApiResult<T> businessError(Integer code, String message) {
        return new ApiResult<>(code, message, null);
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }
}