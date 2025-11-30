package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 训练任务请求DTO
 */
@Data
@Schema(description = "训练任务请求参数")
public class TrainingTaskRequest {
    
    @NotBlank(message = "任务名称不能为空")
    @Schema(description = "任务名称", required = true)
    private String name;
    
    @Schema(description = "任务描述")
    private String description;
    
    @NotNull(message = "模型ID不能为空")
    @Schema(description = "模型ID", required = true)
    private Long modelId;
    
    @Schema(description = "模型名称")
    private String modelName;
    
    @NotNull(message = "数据集ID不能为空")
    @Schema(description = "数据集ID", required = true)
    private Long datasetId;
    
    @Schema(description = "数据集名称")
    private String datasetName;
    
    @Schema(description = "训练配置JSON")
    private String trainingConfig;
    
    @Schema(description = "训练参数JSON")
    private String hyperparameters;
    
    @NotNull(message = "总epoch数不能为空")
    @Schema(description = "总epoch数", required = true)
    private Integer totalEpochs;
    
    @Schema(description = "GPU使用情况")
    private String gpuUsage;
}