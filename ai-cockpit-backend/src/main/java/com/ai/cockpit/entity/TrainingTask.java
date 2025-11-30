package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 训练任务实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "training_task")
public class TrainingTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 任务名称 */
    @Column(nullable = false)
    private String name;
    
    /** 任务描述 */
    private String description;
    
    /** 任务类型：fine-tuning、pre-training、evaluation等 */
    private String taskType;
    
    /** 模型ID */
    private Long modelId;
    
    /** 模型名称 */
    private String modelName;
    
    /** 数据集ID */
    private Long datasetId;
    
    /** 数据集名称 */
    private String datasetName;
    
    /** 训练配置JSON */
    @Column(columnDefinition = "TEXT")
    private String trainingConfig;
    
    /** 训练参数JSON */
    @Column(columnDefinition = "TEXT")
    private String hyperparameters;
    
    /** 训练状态：0-等待中，1-运行中，2-已完成，3-失败，4-已取消 */
    private Integer status;
    
    /** 进度百分比 */
    private Integer progress;
    
    /** 当前epoch */
    private Integer currentEpoch;
    
    /** 总epoch */
    private Integer totalEpochs;
    
    /** 训练指标JSON */
    @Column(columnDefinition = "TEXT")
    private String metrics;
    
    /** 训练日志路径 */
    private String logPath;
    
    /** 输出模型路径 */
    private String outputModelPath;
    
    /** 开始时间 */
    private LocalDateTime startTime;
    
    /** 结束时间 */
    private LocalDateTime endTime;
    
    /** 预计结束时间 */
    private LocalDateTime estimatedEndTime;
    
    /** 训练时长（秒） */
    private Long duration;
    
    /** GPU使用情况 */
    private String gpuUsage;
    
    /** 内存使用情况 */
    private String memoryUsage;
    
    /** 错误信息 */
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
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