package com.ai.cockpit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资源监控请求DTO
 */
@Data
public class ResourceMonitorRequest {
    
    /** 监控类型：cpu-CPU使用率，memory-内存使用率，disk-磁盘使用率，network-网络流量，gpu-GPU使用率，service-服务状态 */
    @NotBlank(message = "监控类型不能为空")
    private String monitorType;
    
    /** 监控目标类型：server-服务器，container-容器，service-服务，database-数据库，cache-缓存 */
    @NotBlank(message = "监控目标类型不能为空")
    private String targetType;
    
    /** 监控目标ID */
    @NotNull(message = "监控目标ID不能为空")
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
    private Integer status = 0;
    
    /** 监控时间戳 */
    @NotNull(message = "监控时间戳不能为空")
    private LocalDateTime monitorTimestamp;
    
    /** 监控数据（JSON格式） */
    private String monitorData;
    
    /** 监控标签（JSON格式） */
    private String monitorTags;
    
    /** 监控描述 */
    private String description;
    
    /** 是否启用 */
    private Boolean isEnabled = true;
    
    /** 监控间隔（秒） */
    private Integer monitorInterval = 60;
    
    /** 监控配置（JSON格式） */
    private String monitorConfig;
    
    /** 告警配置（JSON格式） */
    private String alertConfig;
    
    /** 通知配置（JSON格式） */
    private String notificationConfig;
    
    /** 备注信息 */
    private String remark;
}