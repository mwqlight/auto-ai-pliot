package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI技术方案响应DTO
 */
@Data
@Schema(description = "AI技术方案响应数据")
public class AITechnologySolutionResponse {
    
    @Schema(description = "方案ID")
    private Long id;
    
    @Schema(description = "方案名称")
    private String name;
    
    @Schema(description = "方案代码（唯一标识）")
    private String code;
    
    @Schema(description = "方案描述")
    private String description;
    
    @Schema(description = "技术类型：classification-分类，detection-检测，segmentation-分割，generation-生成，regression-回归")
    private String technologyType;
    
    @Schema(description = "算法类型：CNN-卷积神经网络，RNN-循环神经网络，Transformer-变换器，GAN-生成对抗网络")
    private String algorithmType;
    
    @Schema(description = "适用领域：computer_vision-计算机视觉，nlp-自然语言处理，speech-语音识别，recommendation-推荐系统")
    private String applicationDomain;
    
    @Schema(description = "模型架构")
    private String modelArchitecture;
    
    @Schema(description = "输入数据类型：image，text，audio，video，tabular")
    private String inputDataType;
    
    @Schema(description = "输出数据类型：label，bounding_box，segmentation_mask，text，probability")
    private String outputDataType;
    
    @Schema(description = "预训练模型路径")
    private String pretrainedModelPath;
    
    @Schema(description = "训练配置JSON")
    private String trainingConfig;
    
    @Schema(description = "推理配置JSON")
    private String inferenceConfig;
    
    @Schema(description = "性能指标JSON")
    private String performanceMetrics;
    
    @Schema(description = "准确率")
    private BigDecimal accuracy;
    
    @Schema(description = "精确率")
    private BigDecimal precision;
    
    @Schema(description = "召回率")
    private BigDecimal recall;
    
    @Schema(description = "F1分数")
    private BigDecimal f1Score;
    
    @Schema(description = "推理速度（毫秒）")
    private Integer inferenceSpeed;
    
    @Schema(description = "模型大小（MB）")
    private BigDecimal modelSize;
    
    @Schema(description = "计算复杂度（FLOPs）")
    private Long computationalComplexity;
    
    @Schema(description = "内存消耗（MB）")
    private Integer memoryConsumption;
    
    @Schema(description = "是否公开：0-私有，1-公开")
    private Integer isPublic;
    
    @Schema(description = "状态：0-草稿，1-已发布，2-已下线，3-已归档")
    private Integer status;
    
    @Schema(description = "版本号")
    private String version;
    
    @Schema(description = "标签（逗号分隔）")
    private String tags;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
    
    @Schema(description = "发布时间")
    private LocalDateTime publishedTime;
    
    @Schema(description = "使用次数")
    private Integer usageCount;
    
    @Schema(description = "评分")
    private BigDecimal rating;
    
    @Schema(description = "文档链接")
    private String documentationUrl;
    
    @Schema(description = "代码示例链接")
    private String codeExampleUrl;
    
    @Schema(description = "许可证类型：MIT，Apache，GPL，Commercial")
    private String licenseType;
    
    @Schema(description = "创建者用户名")
    private String creatorUsername;
    
    @Schema(description = "创建者真实姓名")
    private String creatorRealName;
}