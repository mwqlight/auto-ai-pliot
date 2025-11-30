package com.ai.cockpit.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户响应DTO
 */
@Data
public class UserResponse {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 角色列表
     */
    private java.util.List<String> roles;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    /**
     * 登录次数
     */
    private Integer loginCount;
}