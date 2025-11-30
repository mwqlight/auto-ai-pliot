package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 知识图谱实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "knowledge_graph")
public class KnowledgeGraph {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 图谱名称 */
    @Column(nullable = false)
    private String name;
    
    /** 图谱描述 */
    private String description;
    
    /** 图谱类型：domain、general、custom等 */
    private String type;
    
    /** 图谱文件路径 */
    private String graphPath;
    
    /** 实体数量 */
    private Integer entityCount;
    
    /** 关系数量 */
    private Integer relationCount;
    
    /** 图谱状态：0-构建中，1-构建完成，2-已发布，3-已下线 */
    private Integer status;
    
    /** 构建配置JSON */
    @Column(columnDefinition = "TEXT")
    private String buildConfig;
    
    /** 图谱统计信息JSON */
    @Column(columnDefinition = "TEXT")
    private String statistics;
    
    /** 可视化配置JSON */
    @Column(columnDefinition = "TEXT")
    private String visualizationConfig;
    
    /** 创建者ID */
    private Long creatorId;
    
    /** 创建者用户名 */
    private String creatorName;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}