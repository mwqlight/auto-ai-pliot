package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 用户角色关联实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_role", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "role_id"})
})
public class UserRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** 角色ID */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    /** 用户实体 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
    
    /** 角色实体 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;
}