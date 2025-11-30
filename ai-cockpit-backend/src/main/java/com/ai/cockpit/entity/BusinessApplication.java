package com.ai.cockpit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业务应用市场实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "business_application", indexes = {
    @Index(name = "idx_app_code", columnList = "code", unique = true),
    @Index(name = "idx_app_category", columnList = "category"),
    @Index(name = "idx_app_status", columnList = "status"),
    @Index(name = "idx_app_creator", columnList = "creatorId"),
    @Index(name = "idx_app_published_time", columnList = "publishedTime"),
    @Index(name = "idx_app_rating", columnList = "rating"),
    @Index(name = "idx_app_usage_count", columnList = "usageCount")
})
public class BusinessApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 应用名称 */
    @Column(nullable = false, length = 100)
    private String name;
    
    /** 应用代码（唯一标识） */
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    
    /** 应用描述 */
    @Column(length = 1000)
    private String description;
    
    /** 应用分类 */
    @Column(nullable = false, length = 50)
    private String category;
    
    /** 应用子分类 */
    @Column(length = 50)
    private String subCategory;
    
    /** 应用图标 */
    @Column(length = 200)
    private String icon;
    
    /** 应用截图（JSON数组） */
    @Column(columnDefinition = "TEXT")
    private String screenshots;
    
    /** 应用版本 */
    @Column(nullable = false, length = 20)
    private String version;
    
    /** 最低支持版本 */
    @Column(length = 20)
    private String minSupportedVersion;
    
    /** 应用价格（免费为0） */
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    /** 价格单位 */
    @Column(length = 10)
    private String priceUnit;
    
    /** 是否免费 */
    @Column(nullable = false)
    private Boolean isFree = true;
    
    /** 试用天数（0表示无试用） */
    @Column
    private Integer trialDays = 0;
    
    /** 应用配置（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String appConfig;
    
    /** 部署配置（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String deploymentConfig;
    
    /** 依赖服务（JSON数组） */
    @Column(columnDefinition = "TEXT")
    private String dependencies;
    
    /** 应用标签（逗号分隔） */
    @Column(length = 500)
    private String tags;
    
    /** 应用评分 */
    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;
    
    /** 使用次数 */
    @Column
    private Integer usageCount = 0;
    
    /** 下载次数 */
    @Column
    private Integer downloadCount = 0;
    
    /** 收藏次数 */
    @Column
    private Integer favoriteCount = 0;
    
    /** 评论次数 */
    @Column
    private Integer reviewCount = 0;
    
    /** 是否推荐 */
    @Column
    private Boolean isRecommended = false;
    
    /** 是否热门 */
    @Column
    private Boolean isPopular = false;
    
    /** 是否公开 */
    @Column(nullable = false)
    private Boolean isPublic = false;
    
    /** 状态：0-草稿，1-已发布，2-已下线，3-已归档 */
    @Column(nullable = false)
    private Integer status = 0;
    
    /** 创建者ID */
    @Column(nullable = false)
    private Long creatorId;
    
    /** 创建时间 */
    @Column(nullable = false)
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private LocalDateTime updatedTime;
    
    /** 发布时间 */
    @Column
    private LocalDateTime publishedTime;
    
    /** 最后更新者ID */
    @Column
    private Long lastUpdaterId;
    
    /** 应用文档链接 */
    @Column(length = 200)
    private String documentationUrl;
    
    /** 应用演示链接 */
    @Column(length = 200)
    private String demoUrl;
    
    /** 技术支持链接 */
    @Column(length = 200)
    private String supportUrl;
    
    /** 许可证类型 */
    @Column(length = 20)
    private String licenseType;
    
    /** 许可证链接 */
    @Column(length = 200)
    private String licenseUrl;
    
    /** 隐私政策链接 */
    @Column(length = 200)
    private String privacyPolicyUrl;
    
    /** 服务条款链接 */
    @Column(length = 200)
    private String termsOfServiceUrl;
    
    /** 应用大小（MB） */
    @Column(precision = 10, scale = 2)
    private BigDecimal appSize;
    
    /** 安装包链接 */
    @Column(length = 200)
    private String packageUrl;
    
    /** 安装指南 */
    @Column(columnDefinition = "TEXT")
    private String installationGuide;
    
    /** 更新日志 */
    @Column(columnDefinition = "TEXT")
    private String changelog;
    
    /** 系统要求 */
    @Column(columnDefinition = "TEXT")
    private String systemRequirements;
    
    /** 与创建者的关联关系 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private User creator;
}