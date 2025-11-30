package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI技术方案实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ai_technology_solution")
public class AITechnologySolution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 方案名称 */
    @Column(nullable = false)
    private String name;
    
    /** 方案代码（唯一标识） */
    @Column(nullable = false, unique = true)
    private String code;
    
    /** 方案描述 */
    @Column(length = 1000)
    private String description;
    
    /** 技术类型：classification-分类，detection-检测，segmentation-分割，generation-生成，regression-回归 */
    @Column(nullable = false)
    private String technologyType;
    
    /** 算法类型：CNN-卷积神经网络，RNN-循环神经网络，Transformer-变换器，GAN-生成对抗网络 */
    @Column(nullable = false)
    private String algorithmType;
    
    /** 适用领域：computer_vision-计算机视觉，nlp-自然语言处理，speech-语音识别，recommendation-推荐系统 */
    @Column(nullable = false)
    private String applicationDomain;
    
    /** 模型架构：ResNet，BERT，YOLO，GPT等 */
    private String modelArchitecture;
    
    /** 输入数据类型：image，text，audio，video，tabular */
    @Column(nullable = false)
    private String inputDataType;
    
    /** 输出数据类型：label，bounding_box，segmentation_mask，text，probability */
    @Column(nullable = false)
    private String outputDataType;
    
    /** 预训练模型路径 */
    private String pretrainedModelPath;
    
    /** 训练配置JSON */
    @Column(columnDefinition = "TEXT")
    private String trainingConfig;
    
    /** 推理配置JSON */
    @Column(columnDefinition = "TEXT")
    private String inferenceConfig;
    
    /** 性能指标JSON */
    @Column(columnDefinition = "TEXT")
    private String performanceMetrics;
    
    /** 准确率 */
    private BigDecimal accuracy;
    
    /** 精确率 */
    private BigDecimal precision;
    
    /** 召回率 */
    private BigDecimal recall;
    
    /** F1分数 */
    private BigDecimal f1Score;
    
    /** 推理速度（毫秒） */
    private Integer inferenceSpeed;
    
    /** 模型大小（MB） */
    private BigDecimal modelSize;
    
    /** 计算复杂度（FLOPs） */
    private Long computationalComplexity;
    
    /** 内存消耗（MB） */
    private Integer memoryConsumption;
    
    /** 是否公开：0-私有，1-公开 */
    @Column(nullable = false)
    private Integer isPublic = 0;
    
    /** 状态：0-草稿，1-已发布，2-已下线，3-已归档 */
    @Column(nullable = false)
    private Integer status = 0;
    
    /** 版本号 */
    @Column(nullable = false)
    private String version = "1.0.0";
    
    /** 标签（逗号分隔） */
    private String tags;
    
    /** 创建者ID */
    @Column(nullable = false)
    private Long creatorId;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    /** 发布时间 */
    private LocalDateTime publishedTime;
    
    /** 使用次数 */
    @Column(nullable = false)
    private Integer usageCount = 0;
    
    /** 评分 */
    private BigDecimal rating;
    
    /** 文档链接 */
    private String documentationUrl;
    
    /** 代码示例链接 */
    private String codeExampleUrl;
    
    /** 许可证类型：MIT，Apache，GPL，Commercial */
    private String licenseType;
}