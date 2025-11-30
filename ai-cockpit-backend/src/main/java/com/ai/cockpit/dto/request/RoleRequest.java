package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色请求DTO
 */
@Data
@Schema(description = "角色请求")
public class RoleRequest {
    
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称", example = "管理员")
    private String name;
    
    @NotBlank(message = "角色代码不能为空")
    @Schema(description = "角色代码", example = "ADMIN")
    private String code;
    
    @Schema(description = "角色描述", example = "系统管理员")
    private String description;
    
    @NotNull(message = "角色类型不能为空")
    @Schema(description = "角色类型：1-系统角色，2-业务角色", example = "1")
    private Integer type;
    
    @Schema(description = "数据权限范围：1-全部数据，2-本部门数据，3-仅本人数据", example = "1")
    private Integer dataScope = 1;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;
    
    @Schema(description = "权限ID列表")
    private List<Long> permissionIds;
}