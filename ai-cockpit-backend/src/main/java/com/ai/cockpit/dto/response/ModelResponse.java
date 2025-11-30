package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模型响应DTO
 */
@Data
@Schema(description = "模型响应数据")
public class ModelResponse {
    
    @Schema(description = "模型ID")
    private Long id;
    
    @Schema(description = "模型名称")
    private String name;
    
    @Schema(description = "模型描述")
    private String description;
    
    @Schema(description = "模型类型：LLM、Embedding、Vision、Audio等")
    private String type;
    
    @Schema(description = "模型框架：PyTorch、TensorFlow、HuggingFace等")
    private String framework;
    
    @Schema(description = "模型版本")
    private String version;
    
    @Schema(description = "模型文件路径")
    private String modelPath;
    
    @Schema(description = "模型文件大小（字节）")
    private Long fileSize;
    
    @Schema(description = "参数量")
    private Long parameters;
    
    @Schema(description = "输入维度")
    private String inputDimensions;
    
    @Schema(description = "输出维度")
    private String outputDimensions;
    
    @Schema(description = "支持的输入类型")
    private String inputTypes;
    
    @Schema(description = "支持的输出类型")
    private String outputTypes;
    
    @Schema(description = "模型精度：FP32、FP16、INT8等")
    private String precision;
    
    @Schema(description = "模型来源：local、huggingface、modelscope等")
    private String source;
    
    @Schema(description = "来源地址")
    private String sourceUrl;
    
    @Schema(description = "许可证")
    private String license;
    
    @Schema(description = "模型状态：0-未训练，1-训练中，2-训练完成，3-已部署，4-已下线")
    private Integer status;
    
    @Schema(description = "是否公开")
    private Boolean isPublic;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建者用户名")
    private String creatorName;
    
    @Schema(description = "性能指标JSON")
    private String performanceMetrics;
    
    @Schema(description = "模型标签")
    private String tags;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}