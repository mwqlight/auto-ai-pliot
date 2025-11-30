package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色响应DTO
 */
@Data
@Schema(description = "角色响应")
public class RoleResponse {
    
    @Schema(description = "角色ID")
    private Long id;
    
    @Schema(description = "角色名称")
    private String name;
    
    @Schema(description = "角色代码")
    private String code;
    
    @Schema(description = "角色描述")
    private String description;
    
    @Schema(description = "角色类型：1-系统角色，2-业务角色")
    private Integer type;
    
    @Schema(description = "数据权限范围：1-全部数据，2-本部门数据，3-仅本人数据")
    private Integer dataScope;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    @Schema(description = "权限列表")
    private List<PermissionResponse> permissions;
    
    @Schema(description = "用户数量")
    private Integer userCount;
}