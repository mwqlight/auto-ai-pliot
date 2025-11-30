package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限响应DTO
 */
@Data
@Schema(description = "权限响应")
public class PermissionResponse {
    
    @Schema(description = "权限ID")
    private Long id;
    
    @Schema(description = "权限名称")
    private String name;
    
    @Schema(description = "权限代码")
    private String code;
    
    @Schema(description = "权限描述")
    private String description;
    
    @Schema(description = "权限类型：1-菜单，2-按钮，3-接口")
    private Integer type;
    
    @Schema(description = "权限路径")
    private String path;
    
    @Schema(description = "请求方法")
    private String method;
    
    @Schema(description = "父权限ID")
    private Long parentId;
    
    @Schema(description = "排序号")
    private Integer sortOrder;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "子权限列表")
    private List<PermissionResponse> children;
}