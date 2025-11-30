package com.ai.cockpit.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资源监控响应DTO
 */
@Data
public class ResourceMonitorResponse {
    
    private Long id;
    
    /** 监控类型：cpu-CPU使用率，memory-内存使用率，disk-磁盘使用率，network-网络流量，gpu-GPU使用率，service-服务状态 */
    private String monitorType;
    
    /** 监控目标类型：server-服务器，container-容器，service-服务，database-数据库，cache-缓存 */
    private String targetType;
    
    /** 监控目标ID */
    private Long targetId;
    
    /** 监控目标名称 */
    private String targetName;
    
    /** 监控指标值 */
    private BigDecimal metricValue;
    
    /** 监控指标单位 */
    private String metricUnit;
    
    /** 监控阈值（警告） */
    private BigDecimal warningThreshold;
    
    /** 监控阈值（严重） */
    private BigDecimal criticalThreshold;
    
    /** 监控状态：0-正常，1-警告，2-严重，3-未知 */
    private Integer status;
    
    /** 监控状态描述 */
    private String statusDesc;
    
    /** 监控时间戳 */
    private LocalDateTime monitorTimestamp;
    
    /** 监控数据（JSON格式） */
    private String monitorData;
    
    /** 监控标签（JSON格式） */
    private String monitorTags;
    
    /** 监控描述 */
    private String description;
    
    /** 是否启用 */
    private Boolean isEnabled;
    
    /** 监控间隔（秒） */
    private Integer monitorInterval;
    
    /** 最后检查时间 */
    private LocalDateTime lastCheckTime;
    
    /** 最后正常时间 */
    private LocalDateTime lastNormalTime;
    
    /** 最后警告时间 */
    private LocalDateTime lastWarningTime;
    
    /** 最后严重时间 */
    private LocalDateTime lastCriticalTime;
    
    /** 连续警告次数 */
    private Integer consecutiveWarningCount;
    
    /** 连续严重次数 */
    private Integer consecutiveCriticalCount;
    
    /** 总监控次数 */
    private Long totalMonitorCount;
    
    /** 正常次数 */
    private Long normalCount;
    
    /** 警告次数 */
    private Long warningCount;
    
    /** 严重次数 */
    private Long criticalCount;
    
    /** 可用性百分比 */
    private BigDecimal availabilityPercentage;
    
    /** 平均响应时间（毫秒） */
    private BigDecimal avgResponseTime;
    
    /** 最大响应时间（毫秒） */
    private BigDecimal maxResponseTime;
    
    /** 最小响应时间（毫秒） */
    private BigDecimal minResponseTime;
    
    /** 监控配置（JSON格式） */
    private String monitorConfig;
    
    /** 告警配置（JSON格式） */
    private String alertConfig;
    
    /** 通知配置（JSON格式） */
    private String notificationConfig;
    
    /** 创建者ID */
    private Long creatorId;
    
    /** 创建者用户名 */
    private String creatorUsername;
    
    /** 创建者真实姓名 */
    private String creatorRealName;
    
    /** 创建时间 */
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    private LocalDateTime updatedTime;
    
    /** 最后更新者ID */
    private Long lastUpdaterId;
    
    /** 最后更新者用户名 */
    private String lastUpdaterUsername;
    
    /** 最后更新者真实姓名 */
    private String lastUpdaterRealName;
    
    /** 备注信息 */
    private String remark;
}