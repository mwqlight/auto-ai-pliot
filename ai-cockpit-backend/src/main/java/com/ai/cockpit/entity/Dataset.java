package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 数据集实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "dataset")
public class Dataset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 数据集名称 */
    @Column(nullable = false)
    private String name;
    
    /** 数据集描述 */
    private String description;
    
    /** 数据集类型：text、image、audio、video、tabular等 */
    @Column(nullable = false)
    private String type;
    
    /** 数据格式：json、csv、parquet、image等 */
    private String format;
    
    /** 数据集文件路径 */
    private String filePath;
    
    /** 数据集大小（字节） */
    private Long fileSize;
    
    /** 数据条数 */
    private Long dataCount;
    
    /** 数据维度 */
    private String dimensions;
    
    /** 数据质量评分 */
    private BigDecimal qualityScore;
    
    /** 数据来源：local、public、upload等 */
    private String source;
    
    /** 来源地址 */
    private String sourceUrl;
    
    /** 许可证 */
    private String license;
    
    /** 数据集状态：0-未处理，1-处理中，2-处理完成，3-已标注，4-已发布 */
    private Integer status;
    
    /** 是否公开 */
    private Boolean isPublic;
    
    /** 创建者ID */
    private Long creatorId;
    
    /** 创建者用户名 */
    private String creatorName;
    
    /** 数据统计信息JSON */
    @Column(columnDefinition = "TEXT")
    private String statistics;
    
    /** 数据标签 */
    private String tags;
    
    /** 标注进度 */
    private BigDecimal annotationProgress;
    
    /** 训练集比例 */
    private BigDecimal trainRatio;
    
    /** 验证集比例 */
    private BigDecimal validationRatio;
    
    /** 测试集比例 */
    private BigDecimal testRatio;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}