package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户注册请求DTO
 */
@Data
@Schema(description = "用户注册请求")
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "用户名只能包含字母、数字、下划线，长度4-20位")
    @Schema(description = "用户名", example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{6,20}$", message = "密码长度6-20位")
    @Schema(description = "密码", example = "123456")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;
}