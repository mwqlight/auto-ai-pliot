package com.ai.cockpit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资源监控实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "resource_monitor", indexes = {
    @Index(name = "idx_monitor_type", columnList = "monitorType"),
    @Index(name = "idx_monitor_target", columnList = "targetId,targetType"),
    @Index(name = "idx_monitor_status", columnList = "status"),
    @Index(name = "idx_monitor_timestamp", columnList = "monitorTimestamp"),
    @Index(name = "idx_monitor_created_time", columnList = "createdTime")
})
public class ResourceMonitor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 监控类型：cpu-CPU使用率，memory-内存使用率，disk-磁盘使用率，network-网络流量，gpu-GPU使用率，service-服务状态 */
    @Column(nullable = false, length = 20)
    private String monitorType;
    
    /** 监控目标类型：server-服务器，container-容器，service-服务，database-数据库，cache-缓存 */
    @Column(nullable = false, length = 20)
    private String targetType;
    
    /** 监控目标ID */
    @Column(nullable = false)
    private Long targetId;
    
    /** 监控目标名称 */
    @Column(length = 100)
    private String targetName;
    
    /** 监控指标值 */
    @Column(precision = 10, scale = 4)
    private BigDecimal metricValue;
    
    /** 监控指标单位 */
    @Column(length = 10)
    private String metricUnit;
    
    /** 监控阈值（警告） */
    @Column(precision = 10, scale = 4)
    private BigDecimal warningThreshold;
    
    /** 监控阈值（严重） */
    @Column(precision = 10, scale = 4)
    private BigDecimal criticalThreshold;
    
    /** 监控状态：0-正常，1-警告，2-严重，3-未知 */
    @Column(nullable = false)
    private Integer status = 0;
    
    /** 监控时间戳 */
    @Column(nullable = false)
    private LocalDateTime monitorTimestamp;
    
    /** 监控数据（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String monitorData;
    
    /** 监控标签（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String monitorTags;
    
    /** 监控描述 */
    @Column(length = 500)
    private String description;
    
    /** 是否启用 */
    @Column(nullable = false)
    private Boolean isEnabled = true;
    
    /** 监控间隔（秒） */
    @Column
    private Integer monitorInterval = 60;
    
    /** 最后检查时间 */
    @Column
    private LocalDateTime lastCheckTime;
    
    /** 最后正常时间 */
    @Column
    private LocalDateTime lastNormalTime;
    
    /** 最后警告时间 */
    @Column
    private LocalDateTime lastWarningTime;
    
    /** 最后严重时间 */
    @Column
    private LocalDateTime lastCriticalTime;
    
    /** 连续警告次数 */
    @Column
    private Integer consecutiveWarningCount = 0;
    
    /** 连续严重次数 */
    @Column
    private Integer consecutiveCriticalCount = 0;
    
    /** 总监控次数 */
    @Column
    private Long totalMonitorCount = 0L;
    
    /** 正常次数 */
    @Column
    private Long normalCount = 0L;
    
    /** 警告次数 */
    @Column
    private Long warningCount = 0L;
    
    /** 严重次数 */
    @Column
    private Long criticalCount = 0L;
    
    /** 可用性百分比 */
    @Column(precision = 5, scale = 2)
    private BigDecimal availabilityPercentage = BigDecimal.ZERO;
    
    /** 平均响应时间（毫秒） */
    @Column(precision = 10, scale = 2)
    private BigDecimal avgResponseTime = BigDecimal.ZERO;
    
    /** 最大响应时间（毫秒） */
    @Column(precision = 10, scale = 2)
    private BigDecimal maxResponseTime = BigDecimal.ZERO;
    
    /** 最小响应时间（毫秒） */
    @Column(precision = 10, scale = 2)
    private BigDecimal minResponseTime = BigDecimal.ZERO;
    
    /** 监控配置（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String monitorConfig;
    
    /** 告警配置（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String alertConfig;
    
    /** 通知配置（JSON格式） */
    @Column(columnDefinition = "TEXT")
    private String notificationConfig;
    
    /** 创建者ID */
    @Column(nullable = false)
    private Long creatorId;
    
    /** 创建时间 */
    @Column(nullable = false)
    private LocalDateTime createdTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private LocalDateTime updatedTime;
    
    /** 最后更新者ID */
    @Column
    private Long lastUpdaterId;
    
    /** 备注信息 */
    @Column(length = 1000)
    private String remark;
}