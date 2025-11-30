package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据集响应DTO
 */
@Data
@Schema(description = "数据集响应数据")
public class DatasetResponse {
    
    @Schema(description = "数据集ID")
    private Long id;
    
    @Schema(description = "数据集名称")
    private String name;
    
    @Schema(description = "数据集描述")
    private String description;
    
    @Schema(description = "数据集类型：text、image、audio、video、tabular等")
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
    
    @Schema(description = "数据集状态：0-未处理，1-处理中，2-处理完成，3-已标注，4-已发布")
    private Integer status;
    
    @Schema(description = "是否公开")
    private Boolean isPublic;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建者用户名")
    private String creatorName;
    
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
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}