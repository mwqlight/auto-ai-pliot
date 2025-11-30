package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.ResourceMonitorRequest;
import com.ai.cockpit.dto.response.ResourceMonitorResponse;
import com.ai.cockpit.entity.ResourceMonitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 资源监控服务接口
 */
public interface ResourceMonitorService {
    
    /**
     * 创建资源监控
     */
    ResourceMonitorResponse create(ResourceMonitorRequest request, Long creatorId);
    
    /**
     * 更新资源监控
     */
    ResourceMonitorResponse update(Long id, ResourceMonitorRequest request, Long updaterId);
    
    /**
     * 删除资源监控
     */
    void delete(Long id);
    
    /**
     * 根据ID查询资源监控
     */
    ResourceMonitorResponse getById(Long id);
    
    /**
     * 分页查询资源监控
     */
    Page<ResourceMonitorResponse> getPage(Pageable pageable);
    
    /**
     * 根据条件查询资源监控
     */
    Page<ResourceMonitorResponse> search(String monitorType, String targetType, Long targetId, 
                                        Integer status, Boolean isEnabled, LocalDateTime startTime, 
                                        LocalDateTime endTime, Pageable pageable);
    
    /**
     * 批量创建资源监控数据
     */
    void batchCreate(List<ResourceMonitorRequest> requests, Long creatorId);
    
    /**
     * 批量更新资源监控状态
     */
    void batchUpdateStatus(List<Long> ids, Integer status, Long updaterId);
    
    /**
     * 启用/禁用资源监控
     */
    void toggleEnabled(Long id, Boolean isEnabled, Long updaterId);
    
    /**
     * 执行监控检查
     */
    void executeMonitorCheck();
    
    /**
     * 获取监控统计信息
     */
    Map<String, Object> getMonitorStatistics();
    
    /**
     * 获取监控状态分布
     */
    Map<String, Map<Integer, Long>> getStatusDistribution();
    
    /**
     * 获取监控趋势数据
     */
    List<Map<String, Object>> getMonitorTrend(String monitorType, String targetType, 
                                             LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取告警列表
     */
    List<ResourceMonitorResponse> getAlerts();
    
    /**
     * 处理监控告警
     */
    void handleAlert(Long id, String action, String remark, Long handlerId);
    
    /**
     * 清理过期监控数据
     */
    void cleanupExpiredData(LocalDateTime beforeTime);
}