package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 数据集请求DTO
 */
@Data
@Schema(description = "数据集请求参数")
public class DatasetRequest {
    
    @NotBlank(message = "数据集名称不能为空")
    @Schema(description = "数据集名称", required = true)
    private String name;
    
    @Schema(description = "数据集描述")
    private String description;
    
    @NotBlank(message = "数据集类型不能为空")
    @Schema(description = "数据集类型：text、image、audio、video、tabular等", required = true)
    private String type;
    
    @Schema(description = "数据格式：json、csv、parquet、image等")
    private String format;
    
    @Schema(description = "数据集文件路径")
    private String filePath;
    
    @Schema(description = "数据集大小（字节）")
    private Long fileSize;
    
    @Schema(description = "数据条数")
    private Long dataCount;
    
    @Schema(description = "数据维度")
    private String dimensions;
    
    @Schema(description = "数据质量评分")
    private BigDecimal qualityScore;
    
    @Schema(description = "数据来源：local、public、upload等")
    private String source;
    
    @Schema(description = "来源地址")
    private String sourceUrl;
    
    @Schema(description = "许可证")
    private String license;
    
    @Schema(description = "是否公开")
    private Boolean isPublic;
    
    @Schema(description = "数据统计信息JSON")
    private String statistics;
    
    @Schema(description = "数据标签")
    private String tags;
    
    @Schema(description = "标注进度")
    private BigDecimal annotationProgress;
    
    @Schema(description = "训练集比例")
    private BigDecimal trainRatio;
    
    @Schema(description = "验证集比例")
    private BigDecimal validationRatio;
    
    @Schema(description = "测试集比例")
    private BigDecimal testRatio;
}