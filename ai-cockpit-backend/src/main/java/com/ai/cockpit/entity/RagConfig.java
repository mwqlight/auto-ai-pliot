package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * RAG配置实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rag_config")
public class RagConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 配置名称 */
    @Column(nullable = false)
    private String name;
    
    /** 配置描述 */
    private String description;
    
    /** 文档存储路径 */
    private String documentPath;
    
    /** 向量库路径 */
    private String vectorStorePath;
    
    /** Embedding模型ID */
    private Long embeddingModelId;
    
    /** Embedding模型名称 */
    private String embeddingModelName;
    
    /** LLM模型ID */
    private Long llmModelId;
    
    /** LLM模型名称 */
    private String llmModelName;
    
    /** 检索策略：similarity、mmr、filtered等 */
    private String retrievalStrategy;
    
    /** 检索参数JSON */
    @Column(columnDefinition = "TEXT")
    private String retrievalParams;
    
    /** 重排序模型 */
    private String rerankerModel;
    
    /** 配置状态：0-未启用，1-启用中，2-已停用 */
    private Integer status;
    
    /** 文档数量 */
    private Integer documentCount;
    
    /** 向量维度 */
    private Integer vectorDimension;
    
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