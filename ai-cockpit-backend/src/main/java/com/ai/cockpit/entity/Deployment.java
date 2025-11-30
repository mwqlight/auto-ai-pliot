package com.ai.cockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 部署实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "deployment")
public class Deployment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 部署名称 */
    @Column(nullable = false)
    private String name;
    
    /** 部署类型 */
    private String deploymentType;
    
    /** 部署描述 */
    private String description;
    
    /** 模型ID */
    private Long modelId;
    
    /** 模型名称 */
    private String modelName;
    
    /** 部署环境：dev、test、prod */
    private String environment;
    
    /** 部署配置JSON */
    @Column(columnDefinition = "TEXT")
    private String deploymentConfig;
    
    /** API端点 */
    private String apiEndpoint;
    
    /** 服务端口 */
    private Integer servicePort;
    
    /** 部署状态：0-未部署，1-部署中，2-运行中，3-停止，4-失败 */
    private Integer status;
    
    /** 健康检查状态 */
    private String healthStatus;
    
    /** 响应时间（毫秒） */
    private Long responseTime;
    
    /** QPS（每秒查询数） */
    private Integer qps;
    
    /** 错误率 */
    private Double errorRate;
    
    /** 资源使用情况JSON */
    @Column(columnDefinition = "TEXT")
    private String resourceUsage;
    
    /** 部署日志路径 */
    private String logPath;
    
    /** 开始时间 */
    private LocalDateTime startTime;
    
    /** 结束时间 */
    private LocalDateTime endTime;
    
    /** 创建者ID */
    private Long creatorId;
    
    /** 创建者用户名 */
    private String creatorName;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}