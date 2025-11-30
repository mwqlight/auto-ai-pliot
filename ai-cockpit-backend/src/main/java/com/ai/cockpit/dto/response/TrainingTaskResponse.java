package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 训练任务响应DTO
 */
@Data
@Schema(description = "训练任务响应数据")
public class TrainingTaskResponse {
    
    @Schema(description = "任务ID")
    private Long id;
    
    @Schema(description = "任务名称")
    private String name;
    
    @Schema(description = "任务描述")
    private String description;
    
    @Schema(description = "模型ID")
    private Long modelId;
    
    @Schema(description = "模型名称")
    private String modelName;
    
    @Schema(description = "数据集ID")
    private Long datasetId;
    
    @Schema(description = "数据集名称")
    private String datasetName;
    
    @Schema(description = "训练配置JSON")
    private String trainingConfig;
    
    @Schema(description = "训练参数JSON")
    private String hyperparameters;
    
    @Schema(description = "训练状态：0-等待中，1-运行中，2-已完成，3-失败，4-已取消")
    private Integer status;
    
    @Schema(description = "进度百分比")
    private Integer progress;
    
    @Schema(description = "当前epoch")
    private Integer currentEpoch;
    
    @Schema(description = "总epoch")
    private Integer totalEpochs;
    
    @Schema(description = "训练指标JSON")
    private String metrics;
    
    @Schema(description = "训练日志路径")
    private String logPath;
    
    @Schema(description = "输出模型路径")
    private String outputModelPath;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "预计结束时间")
    private LocalDateTime estimatedEndTime;
    
    @Schema(description = "训练时长（秒）")
    private Long duration;
    
    @Schema(description = "GPU使用情况")
    private String gpuUsage;
    
    @Schema(description = "内存使用情况")
    private String memoryUsage;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建者用户名")
    private String creatorName;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}