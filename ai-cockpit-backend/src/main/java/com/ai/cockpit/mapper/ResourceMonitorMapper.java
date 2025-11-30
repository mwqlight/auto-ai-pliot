package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.ResourceMonitorRequest;
import com.ai.cockpit.dto.response.ResourceMonitorResponse;
import com.ai.cockpit.entity.ResourceMonitor;
import org.mapstruct.*;
import java.util.List;

/**
 * 资源监控映射接口
 */
@Mapper(componentModel = "spring")
public interface ResourceMonitorMapper {
    
    /**
     * 请求DTO转实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastCheckTime", ignore = true)
    @Mapping(target = "lastNormalTime", ignore = true)
    @Mapping(target = "lastWarningTime", ignore = true)
    @Mapping(target = "lastCriticalTime", ignore = true)
    @Mapping(target = "consecutiveWarningCount", ignore = true)
    @Mapping(target = "consecutiveCriticalCount", ignore = true)
    @Mapping(target = "totalMonitorCount", ignore = true)
    @Mapping(target = "normalCount", ignore = true)
    @Mapping(target = "warningCount", ignore = true)
    @Mapping(target = "criticalCount", ignore = true)
    @Mapping(target = "availabilityPercentage", ignore = true)
    @Mapping(target = "avgResponseTime", ignore = true)
    @Mapping(target = "maxResponseTime", ignore = true)
    @Mapping(target = "minResponseTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "lastUpdaterId", ignore = true)
    ResourceMonitor toEntity(ResourceMonitorRequest request);
    
    /**
     * 实体转响应DTO
     */
    ResourceMonitorResponse toResponse(ResourceMonitor entity);
    
    /**
     * 更新实体（忽略ID和创建相关字段）
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastCheckTime", ignore = true)
    @Mapping(target = "lastNormalTime", ignore = true)
    @Mapping(target = "lastWarningTime", ignore = true)
    @Mapping(target = "lastCriticalTime", ignore = true)
    @Mapping(target = "consecutiveWarningCount", ignore = true)
    @Mapping(target = "consecutiveCriticalCount", ignore = true)
    @Mapping(target = "totalMonitorCount", ignore = true)
    @Mapping(target = "normalCount", ignore = true)
    @Mapping(target = "warningCount", ignore = true)
    @Mapping(target = "criticalCount", ignore = true)
    @Mapping(target = "availabilityPercentage", ignore = true)
    @Mapping(target = "avgResponseTime", ignore = true)
    @Mapping(target = "maxResponseTime", ignore = true)
    @Mapping(target = "minResponseTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "lastUpdaterId", ignore = true)
    void updateEntity(ResourceMonitorRequest request, @MappingTarget ResourceMonitor entity);
    
    /**
     * 实体列表转响应DTO列表
     */
    List<ResourceMonitorResponse> toResponseList(List<ResourceMonitor> entities);
}