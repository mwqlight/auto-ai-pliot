package com.ai.cockpit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业务应用市场响应DTO
 */
@Data
@Schema(description = "业务应用市场响应数据")
public class BusinessApplicationResponse {
    
    @Schema(description = "应用ID")
    private Long id;
    
    @Schema(description = "应用名称")
    private String name;
    
    @Schema(description = "应用代码（唯一标识）")
    private String code;
    
    @Schema(description = "应用描述")
    private String description;
    
    @Schema(description = "应用分类：customer_service-客服，marketing-营销，finance-金融，healthcare-医疗，education-教育")
    private String category;
    
    @Schema(description = "应用子分类")
    private String subCategory;
    
    @Schema(description = "应用图标URL")
    private String icon;
    
    @Schema(description = "应用截图（JSON数组）")
    private String screenshots;
    
    @Schema(description = "应用版本")
    private String version;
    
    @Schema(description = "最低支持版本")
    private String minSupportedVersion;
    
    @Schema(description = "应用价格（免费为0）")
    private BigDecimal price;
    
    @Schema(description = "价格单位：CNY，USD，EUR")
    private String priceUnit;
    
    @Schema(description = "是否免费")
    private Boolean isFree;
    
    @Schema(description = "试用天数（0表示无试用）")
    private Integer trialDays;
    
    @Schema(description = "应用配置（JSON格式）")
    private String appConfig;
    
    @Schema(description = "部署配置（JSON格式）")
    private String deploymentConfig;
    
    @Schema(description = "依赖服务（JSON数组）")
    private String dependencies;
    
    @Schema(description = "应用标签（逗号分隔）")
    private String tags;
    
    @Schema(description = "应用评分")
    private BigDecimal rating;
    
    @Schema(description = "使用次数")
    private Integer usageCount;
    
    @Schema(description = "下载次数")
    private Integer downloadCount;
    
    @Schema(description = "收藏次数")
    private Integer favoriteCount;
    
    @Schema(description = "评论次数")
    private Integer reviewCount;
    
    @Schema(description = "是否推荐")
    private Boolean isRecommended;
    
    @Schema(description = "是否热门")
    private Boolean isPopular;
    
    @Schema(description = "是否公开")
    private Boolean isPublic;
    
    @Schema(description = "状态：0-草稿，1-已发布，2-已下线，3-已归档")
    private Integer status;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;
    
    @Schema(description = "发布时间")
    private LocalDateTime publishedTime;
    
    @Schema(description = "最后更新者ID")
    private Long lastUpdaterId;
    
    @Schema(description = "应用文档链接")
    private String documentationUrl;
    
    @Schema(description = "应用演示链接")
    private String demoUrl;
    
    @Schema(description = "技术支持链接")
    private String supportUrl;
    
    @Schema(description = "许可证类型")
    private String licenseType;
    
    @Schema(description = "许可证链接")
    private String licenseUrl;
    
    @Schema(description = "隐私政策链接")
    private String privacyPolicyUrl;
    
    @Schema(description = "服务条款链接")
    private String termsOfServiceUrl;
    
    @Schema(description = "应用大小（MB）")
    private BigDecimal appSize;
    
    @Schema(description = "安装包链接")
    private String packageUrl;
    
    @Schema(description = "安装指南")
    private String installationGuide;
    
    @Schema(description = "更新日志")
    private String changelog;
    
    @Schema(description = "系统要求")
    private String systemRequirements;
    
    @Schema(description = "创建者用户名")
    private String creatorUsername;
    
    @Schema(description = "创建者真实姓名")
    private String creatorRealName;
}