package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 角色权限关联实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "role_permission", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role_id", "permission_id"})
})
public class RolePermission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 角色ID */
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    /** 权限ID */
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
    
    /** 角色实体 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;
    
    /** 权限实体 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Permission permission;
}