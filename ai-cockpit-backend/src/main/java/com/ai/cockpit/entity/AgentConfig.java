package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Agent配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "agent_config")
public class AgentConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 配置名称 */
    @Column(nullable = false)
    private String name;
    
    /** 配置描述 */
    private String description;
    
    /** Agent类型：chat、task、workflow等 */
    private String agentType;
    
    /** LLM模型ID */
    private Long llmModelId;
    
    /** LLM模型名称 */
    private String llmModelName;
    
    /** 工具配置JSON */
    @Column(columnDefinition = "TEXT")
    private String toolsConfig;
    
    /** 记忆配置JSON */
    @Column(columnDefinition = "TEXT")
    private String memoryConfig;
    
    /** 思考配置JSON */
    @Column(columnDefinition = "TEXT")
    private String reasoningConfig;
    
    /** 配置状态：0-未启用，1-启用中，2-已停用 */
    private Integer status;
    
    /** 执行超时时间（秒） */
    private Integer timeout;
    
    /** 最大重试次数 */
    private Integer maxRetries;
    
    /** 配置参数JSON */
    @Column(columnDefinition = "TEXT")
    private String configParams;
    
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