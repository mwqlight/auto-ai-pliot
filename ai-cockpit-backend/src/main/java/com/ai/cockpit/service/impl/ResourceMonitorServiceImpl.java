package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.ResourceMonitorRequest;
import com.ai.cockpit.dto.response.ResourceMonitorResponse;
import com.ai.cockpit.entity.ResourceMonitor;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.ResourceMonitorMapper;
import com.ai.cockpit.repository.ResourceMonitorRepository;
import com.ai.cockpit.service.ResourceMonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 资源监控服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceMonitorServiceImpl implements ResourceMonitorService {

    private final ResourceMonitorRepository resourceMonitorRepository;
    private final ResourceMonitorMapper resourceMonitorMapper;

    @Override
    @Transactional
    public ResourceMonitorResponse create(ResourceMonitorRequest request, Long creatorId) {
        // 检查监控项是否已存在
        if (resourceMonitorRepository.findByMonitorTypeAndTargetTypeAndTargetId(
                request.getMonitorType(), request.getTargetType(), request.getTargetId()).isPresent()) {
            throw new BusinessException(30001, "该监控项已存在");
        }
        
        ResourceMonitor monitor = resourceMonitorMapper.toEntity(request);
        monitor.setCreatedTime(LocalDateTime.now());
        monitor.setUpdatedTime(LocalDateTime.now());
        monitor.setCreatorId(creatorId);
        
        ResourceMonitor savedMonitor = resourceMonitorRepository.save(monitor);
        log.info("创建资源监控成功: 类型={}, 目标类型={}, 目标ID={}", 
                request.getMonitorType(), request.getTargetType(), request.getTargetId());
        
        return resourceMonitorMapper.toResponse(savedMonitor);
    }

    @Override
    @Transactional
    public ResourceMonitorResponse update(Long id, ResourceMonitorRequest request, Long updaterId) {
        ResourceMonitor monitor = resourceMonitorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(30002, "监控项不存在"));
        
        resourceMonitorMapper.updateEntity(request, monitor);
        monitor.setUpdatedTime(LocalDateTime.now());
        monitor.setLastUpdaterId(updaterId);
        
        ResourceMonitor updatedMonitor = resourceMonitorRepository.save(monitor);
        log.info("更新资源监控成功: ID={}", id);
        
        return resourceMonitorMapper.toResponse(updatedMonitor);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ResourceMonitor monitor = resourceMonitorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(30002, "监控项不存在"));
        
        resourceMonitorRepository.delete(monitor);
        log.info("删除资源监控成功: ID={}", id);
    }

    @Override
    public ResourceMonitorResponse getById(Long id) {
        ResourceMonitor monitor = resourceMonitorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(30002, "监控项不存在"));
        return resourceMonitorMapper.toResponse(monitor);
    }

    @Override
    public Page<ResourceMonitorResponse> getPage(Pageable pageable) {
        Page<ResourceMonitor> monitors = resourceMonitorRepository.findAll(pageable);
        return monitors.map(resourceMonitorMapper::toResponse);
    }

    @Override
    public Page<ResourceMonitorResponse> search(String monitorType, String targetType, Long targetId, 
                                               Integer status, Boolean isEnabled, LocalDateTime startTime, 
                                               LocalDateTime endTime, Pageable pageable) {
        // 简化实现：使用Repository的默认分页查询
        Page<ResourceMonitor> monitors = resourceMonitorRepository.findAll(pageable);
        return monitors.map(resourceMonitorMapper::toResponse);
    }

    @Override
    @Transactional
    public void batchCreate(List<ResourceMonitorRequest> requests, Long creatorId) {
        for (ResourceMonitorRequest request : requests) {
            create(request, creatorId);
        }
        log.info("批量创建资源监控成功: 数量={}", requests.size());
    }

    @Override
    @Transactional
    public void batchUpdateStatus(List<Long> ids, Integer status, Long updaterId) {
        List<ResourceMonitor> monitors = resourceMonitorRepository.findAllById(ids);
        
        for (ResourceMonitor monitor : monitors) {
            monitor.setStatus(status);
            monitor.setUpdatedTime(LocalDateTime.now());
            monitor.setLastUpdaterId(updaterId);
        }
        
        resourceMonitorRepository.saveAll(monitors);
        log.info("批量更新资源监控状态成功: 数量={}, 状态={}", monitors.size(), status);
    }

    @Override
    @Transactional
    public void toggleEnabled(Long id, Boolean isEnabled, Long updaterId) {
        ResourceMonitor monitor = resourceMonitorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(30002, "监控项不存在"));
        
        monitor.setIsEnabled(isEnabled);
        monitor.setUpdatedTime(LocalDateTime.now());
        monitor.setLastUpdaterId(updaterId);
        
        resourceMonitorRepository.save(monitor);
        log.info("{}资源监控: ID={}", isEnabled ? "启用" : "禁用", id);
    }

    @Override
    @Transactional
    public void executeMonitorCheck() {
        LocalDateTime checkTime = LocalDateTime.now().minusMinutes(5);
        List<ResourceMonitor> monitors = resourceMonitorRepository.findMonitorsNeedCheck(checkTime);
        
        for (ResourceMonitor monitor : monitors) {
            // 模拟监控检查逻辑
            monitor.setLastCheckTime(LocalDateTime.now());
            
            // 这里应该实现实际的监控检查逻辑
            // 例如：调用API检查服务状态、检查资源使用率等
            
            resourceMonitorRepository.save(monitor);
        }
        
        log.info("执行监控检查完成: 检查数量={}", monitors.size());
    }

    @Override
    public Map<String, Object> getMonitorStatistics() {
        // 简化实现：返回基本统计信息
        return Map.of(
                "totalCount", resourceMonitorRepository.count(),
                "enabledCount", resourceMonitorRepository.countByIsEnabled(true),
                "warningCount", resourceMonitorRepository.countByStatus(1),
                "criticalCount", resourceMonitorRepository.countByStatus(2)
        );
    }

    @Override
    public Map<String, Map<Integer, Long>> getStatusDistribution() {
        // 简化实现：返回状态分布
        return Map.of(
                "monitorType", Map.of(
                        0, resourceMonitorRepository.countByStatus(0),
                        1, resourceMonitorRepository.countByStatus(1),
                        2, resourceMonitorRepository.countByStatus(2)
                )
        );
    }

    @Override
    public List<Map<String, Object>> getMonitorTrend(String monitorType, String targetType, 
                                                    LocalDateTime startTime, LocalDateTime endTime) {
        // 简化实现：返回趋势数据
        return List.of(
                Map.of("timestamp", startTime, "value", 95.5),
                Map.of("timestamp", endTime, "value", 96.2)
        );
    }

    @Override
    public List<ResourceMonitorResponse> getAlerts() {
        List<ResourceMonitor> alerts = resourceMonitorRepository.findWarningOrCriticalMonitors();
        return resourceMonitorMapper.toResponseList(alerts);
    }

    @Override
    @Transactional
    public void handleAlert(Long id, String action, String remark, Long handlerId) {
        ResourceMonitor monitor = resourceMonitorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(30002, "监控项不存在"));
        
        // 处理告警逻辑
        if ("resolve".equals(action)) {
            monitor.setStatus(0); // 恢复正常
        } else if ("ignore".equals(action)) {
            // 忽略告警，保持当前状态
        }
        
        monitor.setUpdatedTime(LocalDateTime.now());
        monitor.setLastUpdaterId(handlerId);
        
        resourceMonitorRepository.save(monitor);
        log.info("处理监控告警成功: ID={}, 操作={}", id, action);
    }

    @Override
    @Transactional
    public void cleanupExpiredData(LocalDateTime beforeTime) {
        List<ResourceMonitor> expiredData = resourceMonitorRepository.findByMonitorTimestampBefore(beforeTime);
        
        if (!expiredData.isEmpty()) {
            resourceMonitorRepository.deleteAll(expiredData);
            log.info("清理过期监控数据成功: 数量={}", expiredData.size());
        }
    }
}