package com.ai.cockpit.dto.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户请求DTO
 */
@Data
public class UserRequest {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;
    
    /**
     * 密码
     */
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 手机号
     */
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    private String phone;
    
    /**
     * 真实姓名
     */
    @Size(max = 20, message = "真实姓名长度不能超过20个字符")
    private String realName;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 角色ID列表
     */
    private java.util.List<Long> roleIds;
}