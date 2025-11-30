package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 权限实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "permission")
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 权限名称 */
    @Column(nullable = false, unique = true)
    private String name;
    
    /** 权限代码（唯一标识） */
    @Column(nullable = false, unique = true)
    private String code;
    
    /** 权限描述 */
    private String description;
    
    /** 权限类型：MENU-菜单权限，BUTTON-按钮权限，API-接口权限，DATA-数据权限 */
    @Column(nullable = false)
    private String type;
    
    /** 权限路径（菜单路径或API路径） */
    private String path;
    
    /** 请求方法（GET/POST/PUT/DELETE等） */
    private String method;
    
    /** 父权限ID */
    private Long parentId;
    
    /** 排序号 */
    private Integer sortOrder;
    
    /** 图标 */
    private String icon;
    
    /** 是否启用 */
    @Column(nullable = false)
    private Boolean enabled = true;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}