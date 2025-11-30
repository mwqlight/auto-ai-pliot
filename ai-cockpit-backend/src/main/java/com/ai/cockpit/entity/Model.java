package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 模型实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "model")
public class Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 模型名称 */
    @Column(nullable = false)
    private String name;
    
    /** 模型描述 */
    private String description;
    
    /** 模型类型：LLM、Embedding、Vision、Audio等 */
    @Column(nullable = false)
    private String type;
    
    /** 模型框架：PyTorch、TensorFlow、HuggingFace等 */
    private String framework;
    
    /** 模型版本 */
    private String version;
    
    /** 模型文件路径 */
    @Column(name = "file_path")
    private String filePath;
    
    /** 模型文件大小（字节） */
    @Column(name = "file_size")
    private Long fileSize;
    
    /** 参数量 */
    private Long parameters;
    
    /** 准确率 */
    private BigDecimal accuracy;
    
    /** 模型状态：0-未训练，1-训练中，2-训练完成，3-已部署，4-已下线 */
    private Integer status;
    
    /** 创建者ID */
    @Column(name = "created_by")
    private Long createdBy;
    
    /** 创建时间 */
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
}