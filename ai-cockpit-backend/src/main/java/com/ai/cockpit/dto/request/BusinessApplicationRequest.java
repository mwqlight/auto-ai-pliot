package com.ai.cockpit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 业务应用市场请求DTO
 */
@Data
@Schema(description = "业务应用市场请求参数")
public class BusinessApplicationRequest {
    
    @NotBlank(message = "应用名称不能为空")
    @Size(max = 100, message = "应用名称长度不能超过100个字符")
    @Schema(description = "应用名称", example = "智能客服系统")
    private String name;
    
    @NotBlank(message = "应用代码不能为空")
    @Size(max = 50, message = "应用代码长度不能超过50个字符")
    @Schema(description = "应用代码（唯一标识）", example = "smart_customer_service")
    private String code;
    
    @Size(max = 1000, message = "应用描述长度不能超过1000个字符")
    @Schema(description = "应用描述", example = "基于AI的智能客服系统，支持自动问答和情感分析")
    private String description;
    
    @NotBlank(message = "应用分类不能为空")
    @Schema(description = "应用分类：customer_service-客服，marketing-营销，finance-金融，healthcare-医疗，education-教育", 
             example = "customer_service")
    private String category;
    
    @Schema(description = "应用子分类", example = "chatbot")
    private String subCategory;
    
    @Schema(description = "应用图标URL", example = "/icons/customer-service.png")
    private String icon;
    
    @Schema(description = "应用截图（JSON数组）")
    private String screenshots;
    
    @NotBlank(message = "应用版本不能为空")
    @Schema(description = "应用版本", example = "1.0.0")
    private String version;
    
    @Schema(description = "最低支持版本", example = "1.0.0")
    private String minSupportedVersion;
    
    @Schema(description = "应用价格（免费为0）", example = "0.00")
    private BigDecimal price = BigDecimal.ZERO;
    
    @Schema(description = "价格单位：CNY，USD，EUR", example = "CNY")
    private String priceUnit = "CNY";
    
    @NotNull(message = "是否免费不能为空")
    @Schema(description = "是否免费", example = "true")
    private Boolean isFree = true;
    
    @Schema(description = "试用天数（0表示无试用）", example = "7")
    private Integer trialDays = 0;
    
    @Schema(description = "应用配置（JSON格式）")
    private String appConfig;
    
    @Schema(description = "部署配置（JSON格式）")
    private String deploymentConfig;
    
    @Schema(description = "依赖服务（JSON数组）")
    private String dependencies;
    
    @Schema(description = "应用标签（逗号分隔）", example = "智能客服,AI,自然语言处理")
    private String tags;
    
    @Schema(description = "是否推荐", example = "false")
    private Boolean isRecommended = false;
    
    @Schema(description = "是否公开", example = "false")
    private Boolean isPublic = false;
    
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-草稿，1-已发布，2-已下线，3-已归档", example = "0")
    private Integer status = 0;
    
    @Schema(description = "应用文档链接", example = "https://docs.example.com/smart-customer-service")
    private String documentationUrl;
    
    @Schema(description = "应用演示链接", example = "https://demo.example.com/smart-customer-service")
    private String demoUrl;
    
    @Schema(description = "技术支持链接", example = "https://support.example.com")
    private String supportUrl;
    
    @Schema(description = "许可证类型：MIT，Apache，GPL，Commercial", example = "MIT")
    private String licenseType;
    
    @Schema(description = "许可证链接", example = "https://license.example.com/mit")
    private String licenseUrl;
    
    @Schema(description = "隐私政策链接", example = "https://privacy.example.com")
    private String privacyPolicyUrl;
    
    @Schema(description = "服务条款链接", example = "https://terms.example.com")
    private String termsOfServiceUrl;
    
    @Schema(description = "应用大小（MB）", example = "50.5")
    private BigDecimal appSize;
    
    @Schema(description = "安装包链接", example = "https://download.example.com/smart-customer-service.zip")
    private String packageUrl;
    
    @Schema(description = "安装指南")
    private String installationGuide;
    
    @Schema(description = "更新日志")
    private String changelog;
    
    @Schema(description = "系统要求")
    private String systemRequirements;
}