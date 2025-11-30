package com.ai.cockpit.controller;

import com.ai.cockpit.dto.request.ResourceMonitorRequest;
import com.ai.cockpit.dto.response.ResourceMonitorResponse;
import com.ai.cockpit.service.ResourceMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 资源监控控制器
 */
@Tag(name = "资源监控管理", description = "资源监控相关接口")
@RestController
@RequestMapping("/api/v1/resource-monitors")
@RequiredArgsConstructor
public class ResourceMonitorController {
    
    private final ResourceMonitorService resourceMonitorService;
    
    @Operation(summary = "创建资源监控")
    @PostMapping
    public ResponseEntity<ResourceMonitorResponse> create(
            @Valid @RequestBody ResourceMonitorRequest request,
            @Parameter(hidden = true) @RequestAttribute Long userId) {
        ResourceMonitorResponse response = resourceMonitorService.create(request, userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "更新资源监控")
    @PutMapping("/{id}")
    public ResponseEntity<ResourceMonitorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ResourceMonitorRequest request,
            @Parameter(hidden = true) @RequestAttribute Long userId) {
        ResourceMonitorResponse response = resourceMonitorService.update(id, request, userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "删除资源监控")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceMonitorService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "根据ID查询资源监控")
    @GetMapping("/{id}")
    public ResponseEntity<ResourceMonitorResponse> getById(@PathVariable Long id) {
        ResourceMonitorResponse response = resourceMonitorService.getById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "分页查询资源监控")
    @GetMapping
    public ResponseEntity<Page<ResourceMonitorResponse>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdTime") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<ResourceMonitorResponse> response = resourceMonitorService.getPage(pageable);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "条件查询资源监控")
    @GetMapping("/search")
    public ResponseEntity<Page<ResourceMonitorResponse>> search(
            @RequestParam(required = false) String monitorType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) Long targetId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Boolean isEnabled,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "monitorTimestamp") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<ResourceMonitorResponse> response = resourceMonitorService.search(
                monitorType, targetType, targetId, status, isEnabled, startTime, endTime, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "批量创建资源监控")
    @PostMapping("/batch")
    public ResponseEntity<Void> batchCreate(
            @Valid @RequestBody List<ResourceMonitorRequest> requests,
            @Parameter(hidden = true) @RequestAttribute Long userId) {
        resourceMonitorService.batchCreate(requests, userId);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "批量更新资源监控状态")
    @PutMapping("/batch/status")
    public ResponseEntity<Void> batchUpdateStatus(
            @RequestBody List<Long> ids,
            @RequestParam Integer status,
            @Parameter(hidden = true) @RequestAttribute Long userId) {
        resourceMonitorService.batchUpdateStatus(ids, status, userId);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "启用/禁用资源监控")
    @PutMapping("/{id}/toggle-enabled")
    public ResponseEntity<Void> toggleEnabled(
            @PathVariable Long id,
            @RequestParam Boolean isEnabled,
            @Parameter(hidden = true) @RequestAttribute Long userId) {
        resourceMonitorService.toggleEnabled(id, isEnabled, userId);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "执行监控检查")
    @PostMapping("/execute-check")
    public ResponseEntity<Void> executeMonitorCheck() {
        resourceMonitorService.executeMonitorCheck();
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "获取监控统计信息")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMonitorStatistics() {
        Map<String, Object> statistics = resourceMonitorService.getMonitorStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    @Operation(summary = "获取监控状态分布")
    @GetMapping("/status-distribution")
    public ResponseEntity<Map<String, Map<Integer, Long>>> getStatusDistribution() {
        Map<String, Map<Integer, Long>> distribution = resourceMonitorService.getStatusDistribution();
        return ResponseEntity.ok(distribution);
    }
    
    @Operation(summary = "获取监控趋势数据")
    @GetMapping("/trend")
    public ResponseEntity<List<Map<String, Object>>> getMonitorTrend(
            @RequestParam String monitorType,
            @RequestParam String targetType,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        
        List<Map<String, Object>> trendData = resourceMonitorService.getMonitorTrend(
                monitorType, targetType, startTime, endTime);
        return ResponseEntity.ok(trendData);
    }
    
    @Operation(summary = "获取告警列表")
    @GetMapping("/alerts")
    public ResponseEntity<List<ResourceMonitorResponse>> getAlerts() {
        List<ResourceMonitorResponse> alerts = resourceMonitorService.getAlerts();
        return ResponseEntity.ok(alerts);
    }
    
    @Operation(summary = "处理监控告警")
    @PutMapping("/{id}/handle-alert")
    public ResponseEntity<Void> handleAlert(
            @PathVariable Long id,
            @RequestParam String action,
            @RequestParam(required = false) String remark,
            @Parameter(hidden = true) @RequestAttribute Long userId) {
        
        resourceMonitorService.handleAlert(id, action, remark, userId);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "清理过期监控数据")
    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanupExpiredData(@RequestParam LocalDateTime beforeTime) {
        resourceMonitorService.cleanupExpiredData(beforeTime);
        return ResponseEntity.ok().build();
    }
}