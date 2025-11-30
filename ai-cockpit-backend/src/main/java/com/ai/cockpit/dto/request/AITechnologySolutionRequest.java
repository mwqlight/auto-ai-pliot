package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * AI技术方案请求DTO
 */
@Data
@Schema(description = "AI技术方案请求参数")
public class AITechnologySolutionRequest {
    
    @NotBlank(message = "方案名称不能为空")
    @Size(max = 100, message = "方案名称长度不能超过100个字符")
    @Schema(description = "方案名称", example = "图像分类-ResNet50")
    private String name;
    
    @NotBlank(message = "方案代码不能为空")
    @Size(max = 50, message = "方案代码长度不能超过50个字符")
    @Schema(description = "方案代码（唯一标识）", example = "image_classification_resnet50")
    private String code;
    
    @Size(max = 1000, message = "方案描述长度不能超过1000个字符")
    @Schema(description = "方案描述", example = "基于ResNet50的图像分类解决方案")
    private String description;
    
    @NotBlank(message = "技术类型不能为空")
    @Schema(description = "技术类型：classification-分类，detection-检测，segmentation-分割，generation-生成，regression-回归", 
             example = "classification")
    private String technologyType;
    
    @NotBlank(message = "算法类型不能为空")
    @Schema(description = "算法类型：CNN-卷积神经网络，RNN-循环神经网络，Transformer-变换器，GAN-生成对抗网络", 
             example = "CNN")
    private String algorithmType;
    
    @NotBlank(message = "适用领域不能为空")
    @Schema(description = "适用领域：computer_vision-计算机视觉，nlp-自然语言处理，speech-语音识别，recommendation-推荐系统", 
             example = "computer_vision")
    private String applicationDomain;
    
    @Schema(description = "模型架构", example = "ResNet50")
    private String modelArchitecture;
    
    @NotBlank(message = "输入数据类型不能为空")
    @Schema(description = "输入数据类型：image，text，audio，video，tabular", example = "image")
    private String inputDataType;
    
    @NotBlank(message = "输出数据类型不能为空")
    @Schema(description = "输出数据类型：label，bounding_box，segmentation_mask，text，probability", 
             example = "label")
    private String outputDataType;
    
    @Schema(description = "预训练模型路径", example = "/models/resnet50.pth")
    private String pretrainedModelPath;
    
    @Schema(description = "训练配置JSON")
    private String trainingConfig;
    
    @Schema(description = "推理配置JSON")
    private String inferenceConfig;
    
    @Schema(description = "性能指标JSON")
    private String performanceMetrics;
    
    @Schema(description = "准确率", example = "0.95")
    private BigDecimal accuracy;
    
    @Schema(description = "精确率", example = "0.94")
    private BigDecimal precision;
    
    @Schema(description = "召回率", example = "0.96")
    private BigDecimal recall;
    
    @Schema(description = "F1分数", example = "0.95")
    private BigDecimal f1Score;
    
    @Schema(description = "推理速度（毫秒）", example = "50")
    private Integer inferenceSpeed;
    
    @Schema(description = "模型大小（MB）", example = "98.5")
    private BigDecimal modelSize;
    
    @Schema(description = "计算复杂度（FLOPs）", example = "4110000000")
    private Long computationalComplexity;
    
    @Schema(description = "内存消耗（MB）", example = "256")
    private Integer memoryConsumption;
    
    @NotNull(message = "是否公开不能为空")
    @Schema(description = "是否公开：0-私有，1-公开", example = "0")
    private Integer isPublic;
    
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-草稿，1-已发布，2-已下线，3-已归档", example = "0")
    private Integer status;
    
    @NotBlank(message = "版本号不能为空")
    @Schema(description = "版本号", example = "1.0.0")
    private String version;
    
    @Schema(description = "标签（逗号分隔）", example = "图像分类,深度学习,计算机视觉")
    private String tags;
    
    @Schema(description = "文档链接", example = "https://docs.example.com/resnet50")
    private String documentationUrl;
    
    @Schema(description = "代码示例链接", example = "https://github.com/example/resnet50-demo")
    private String codeExampleUrl;
    
    @Schema(description = "许可证类型：MIT，Apache，GPL，Commercial", example = "MIT")
    private String licenseType;
}