package com.ai.cockpit.exception;

import com.ai.cockpit.dto.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Object> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("参数校验异常: {}", errors);
        return ApiResult.error(400, "参数校验失败", errors);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Map<String, String>> handleBindException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("参数绑定异常: {}", errors);
        return ApiResult.error(400, "参数绑定失败", errors);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("约束违反异常: {}", e.getMessage());
        return ApiResult.error(400, e.getMessage());
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<String> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证异常: {}", e.getMessage());
        return ApiResult.error(401, "认证失败");
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<String> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足异常: {}", e.getMessage());
        return ApiResult.error(403, "权限不足");
    }

    /**
     * 处理密码错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult<String> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("密码错误异常: {}", e.getMessage());
        return ApiResult.error(401, "用户名或密码错误");
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<String> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {} {}", request.getMethod(), request.getRequestURI(), e);
        return ApiResult.error(500, "系统异常，请联系管理员");
    }
}