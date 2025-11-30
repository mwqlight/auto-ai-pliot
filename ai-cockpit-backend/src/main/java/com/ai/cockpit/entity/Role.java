package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 角色名称 */
    @Column(nullable = false, unique = true)
    private String name;
    
    /** 角色代码（唯一标识） */
    @Column(nullable = false, unique = true)
    private String code;
    
    /** 角色描述 */
    private String description;
    
    /** 角色类型：SYSTEM-系统角色，CUSTOM-自定义角色 */
    @Column(nullable = false)
    private String type = "CUSTOM";
    
    /** 是否启用 */
    @Column(nullable = false)
    private Boolean enabled = true;
    
    /** 数据权限范围：ALL-全部，DEPT-本部门，SELF-仅自己 */
    private String dataScope = "ALL";
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    /** 角色权限关联（一对多） */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RolePermission> permissions;
}