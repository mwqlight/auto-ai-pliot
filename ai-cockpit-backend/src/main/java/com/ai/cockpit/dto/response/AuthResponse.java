package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 认证响应DTO
 */
@Data
@Schema(description = "认证响应")
public class AuthResponse {

    @Schema(description = "访问token")
    private String accessToken;

    @Schema(description = "刷新token")
    private String refreshToken;

    @Schema(description = "token类型")
    private String tokenType = "Bearer";

    @Schema(description = "过期时间（秒）")
    private Long expiresIn;

    @Schema(description = "用户信息")
    private UserInfo userInfo;

    @Data
    @Schema(description = "用户信息")
    public static class UserInfo {
        
        @Schema(description = "用户ID")
        private Long id;
        
        @Schema(description = "用户名")
        private String username;
        
        @Schema(description = "邮箱")
        private String email;
        
        @Schema(description = "手机号")
        private String phone;
        
        @Schema(description = "角色列表")
        private String[] roles;
        
        @Schema(description = "权限列表")
        private String[] permissions;
    }
}