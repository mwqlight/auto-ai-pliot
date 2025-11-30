package com.ai.cockpit.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private Integer code;

    /** 错误信息 */
    private String message;

    public BusinessException(String message) {
        super(message);
        this.code = 10000;
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 用户相关异常（10000-10999）
     */
    public static BusinessException userNotFound() {
        return new BusinessException(10001, "用户不存在");
    }

    public static BusinessException userAlreadyExists() {
        return new BusinessException(10002, "用户已存在");
    }

    public static BusinessException passwordError() {
        return new BusinessException(10003, "密码错误");
    }

    public static BusinessException accountDisabled() {
        return new BusinessException(10004, "账号已禁用");
    }

    /**
     * 模型相关异常（11000-11999）
     */
    public static BusinessException modelNotFound() {
        return new BusinessException(11001, "模型不存在");
    }

    public static BusinessException modelAlreadyExists() {
        return new BusinessException(11002, "模型已存在");
    }

    public static BusinessException modelTrainingFailed() {
        return new BusinessException(11003, "模型训练失败");
    }

    public static BusinessException modelDeploymentFailed() {
        return new BusinessException(11004, "模型部署失败");
    }

    /**
     * 数据集相关异常（12000-12999）
     */
    public static BusinessException datasetNotFound() {
        return new BusinessException(12001, "数据集不存在");
    }

    public static BusinessException datasetAlreadyExists() {
        return new BusinessException(12002, "数据集已存在");
    }

    public static BusinessException datasetFormatError() {
        return new BusinessException(12003, "数据集格式错误");
    }

    /**
     * 训练任务相关异常（13000-13999）
     */
    public static BusinessException trainingTaskNotFound() {
        return new BusinessException(13001, "训练任务不存在");
    }

    public static BusinessException trainingTaskAlreadyRunning() {
        return new BusinessException(13002, "训练任务正在运行");
    }

    public static BusinessException trainingTaskFailed() {
        return new BusinessException(13003, "训练任务执行失败");
    }

    /**
     * 部署相关异常（14000-14999）
     */
    public static BusinessException deploymentNotFound() {
        return new BusinessException(14001, "部署不存在");
    }

    public static BusinessException deploymentAlreadyExists() {
        return new BusinessException(14002, "部署已存在");
    }

    public static BusinessException deploymentFailed() {
        return new BusinessException(14003, "部署失败");
    }

    /**
     * 权限相关异常（15000-15999）
     */
    public static BusinessException permissionDenied() {
        return new BusinessException(15001, "权限不足");
    }

    public static BusinessException roleNotFound() {
        return new BusinessException(15002, "角色不存在");
    }

    /**
     * 系统相关异常（16000-16999）
     */
    public static BusinessException systemError() {
        return new BusinessException(16001, "系统错误");
    }

    public static BusinessException resourceNotAvailable() {
        return new BusinessException(16002, "资源不可用");
    }

    public static BusinessException fileUploadFailed() {
        return new BusinessException(16003, "文件上传失败");
    }

    /**
     * 参数校验异常（17000-17999）
     */
    public static BusinessException parameterError(String message) {
        return new BusinessException(17001, message);
    }

    public static BusinessException parameterRequired(String paramName) {
        return new BusinessException(17002, "参数" + paramName + "不能为空");
    }

    public static BusinessException parameterInvalid(String paramName) {
        return new BusinessException(17003, "参数" + paramName + "无效");
    }
}