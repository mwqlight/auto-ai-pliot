package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 用户名 */
    @Column(unique = true, nullable = false)
    private String username;
    
    /** 邮箱 */
    @Column(unique = true)
    private String email;
    
    /** 密码（加密存储） */
    @Column(nullable = false)
    private String password;
    
    /** 昵称 */
    private String nickname;
    
    /** 头像URL */
    private String avatar;
    
    /** 手机号 */
    private String phone;
    
    /** 用户状态：0-禁用，1-启用 */
    private Integer status;
    
    /** 用户角色 */
    private String role;
    
    /** 用户角色关联（一对多） */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserRole> userRoles;
    
    /** 最后登录时间 */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    /** 最后登录IP */
    @Column(name = "last_login_ip")
    private String lastLoginIp;
    
    /** 创建时间 */
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 获取用户角色列表
     * @return 角色代码列表
     */
    public List<String> getRoles() {
        if (userRoles == null) {
            return new ArrayList<>();
        }
        return userRoles.stream()
            .map(userRole -> userRole.getRole() != null ? userRole.getRole().getCode() : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}