package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 权限请求DTO
 */
@Data
@Schema(description = "权限请求")
public class PermissionRequest {
    
    @NotBlank(message = "权限名称不能为空")
    @Schema(description = "权限名称", example = "用户管理")
    private String name;
    
    @NotBlank(message = "权限代码不能为空")
    @Schema(description = "权限代码", example = "user:manage")
    private String code;
    
    @Schema(description = "权限描述", example = "用户管理相关权限")
    private String description;
    
    @NotNull(message = "权限类型不能为空")
    @Schema(description = "权限类型：1-菜单，2-按钮，3-接口", example = "1")
    private Integer type;
    
    @Schema(description = "权限路径", example = "/user")
    private String path;
    
    @Schema(description = "请求方法：GET,POST,PUT,DELETE等", example = "GET")
    private String method;
    
    @Schema(description = "父权限ID", example = "0")
    private Long parentId;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;
}