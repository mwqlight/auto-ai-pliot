package com.ai.cockpit.controller;

import com.ai.cockpit.dto.request.DatasetRequest;
import com.ai.cockpit.dto.response.DatasetResponse;
import com.ai.cockpit.service.DatasetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据集控制器
 */
@Tag(name = "数据集管理", description = "数据集的创建、查询、更新、删除等操作")
@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
public class DatasetController {
    
    private final DatasetService datasetService;
    
    @Operation(summary = "创建数据集")
    @PostMapping
    public ResponseEntity<DatasetResponse> createDataset(
            @Validated @RequestBody DatasetRequest request) {
        DatasetResponse response = datasetService.createDataset(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "更新数据集")
    @PutMapping("/{id}")
    public ResponseEntity<DatasetResponse> updateDataset(
            @Parameter(description = "数据集ID") @PathVariable Long id,
            @Validated @RequestBody DatasetRequest request) {
        DatasetResponse response = datasetService.updateDataset(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "删除数据集")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataset(
            @Parameter(description = "数据集ID") @PathVariable Long id) {
        datasetService.deleteDataset(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "根据ID查询数据集")
    @GetMapping("/{id}")
    public ResponseEntity<DatasetResponse> getDatasetById(
            @Parameter(description = "数据集ID") @PathVariable Long id) {
        DatasetResponse response = datasetService.findById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "根据名称查询数据集")
    @GetMapping("/name/{name}")
    public ResponseEntity<DatasetResponse> getDatasetByName(
            @Parameter(description = "数据集名称") @PathVariable String name) {
        DatasetResponse response = datasetService.findByName(name);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "获取所有数据集列表")
    @GetMapping
    public ResponseEntity<List<DatasetResponse>> getAllDatasets() {
        List<DatasetResponse> responses = datasetService.findAllDatasets();
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据类型查询数据集列表")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<DatasetResponse>> getDatasetsByType(
            @Parameter(description = "数据集类型：text、image、audio、video、tabular等") 
            @PathVariable String type) {
        List<DatasetResponse> responses = datasetService.findByType(type);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据状态查询数据集列表")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DatasetResponse>> getDatasetsByStatus(
            @Parameter(description = "数据集状态：0-未处理，1-处理中，2-处理完成，3-已标注，4-已发布") 
            @PathVariable Integer status) {
        List<DatasetResponse> responses = datasetService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据创建者ID查询数据集列表")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<DatasetResponse>> getDatasetsByCreatorId(
            @Parameter(description = "创建者ID") @PathVariable Long creatorId) {
        List<DatasetResponse> responses = datasetService.findByCreatorId(creatorId);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据数据来源查询数据集列表")
    @GetMapping("/source/{source}")
    public ResponseEntity<List<DatasetResponse>> getDatasetsBySource(
            @Parameter(description = "数据来源：local、public、upload等") @PathVariable String source) {
        List<DatasetResponse> responses = datasetService.findBySource(source);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据标签查询数据集列表")
    @GetMapping("/tags/{tag}")
    public ResponseEntity<List<DatasetResponse>> getDatasetsByTag(
            @Parameter(description = "标签关键词") @PathVariable String tag) {
        List<DatasetResponse> responses = datasetService.findByTagsContaining(tag);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据名称或描述搜索数据集")
    @GetMapping("/search")
    public ResponseEntity<List<DatasetResponse>> searchDatasets(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        List<DatasetResponse> responses = datasetService.searchByNameOrDescription(keyword);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "获取公开数据集列表")
    @GetMapping("/public")
    public ResponseEntity<List<DatasetResponse>> getPublicDatasets() {
        List<DatasetResponse> responses = datasetService.findPublicDatasets();
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "更新数据集状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @Parameter(description = "数据集ID") @PathVariable Long id,
            @RequestParam Integer status) {
        datasetService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "更新数据集统计信息")
    @PutMapping("/{id}/statistics")
    public ResponseEntity<Void> updateStatistics(
            @Parameter(description = "数据集ID") @PathVariable Long id,
            @RequestBody String statistics) {
        datasetService.updateStatistics(id, statistics);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "更新数据集标注进度")
    @PutMapping("/{id}/annotation-progress")
    public ResponseEntity<Void> updateAnnotationProgress(
            @Parameter(description = "数据集ID") @PathVariable Long id,
            @RequestParam BigDecimal progress) {
        datasetService.updateAnnotationProgress(id, progress);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "统计数据集数量")
    @GetMapping("/count")
    public ResponseEntity<Long> countDatasets() {
        long count = datasetService.countDatasets();
        return ResponseEntity.ok(count);
    }
    
    @Operation(summary = "统计不同类型数据集数量")
    @GetMapping("/count/type/{type}")
    public ResponseEntity<Long> countByType(
            @Parameter(description = "数据集类型") @PathVariable String type) {
        long count = datasetService.countByType(type);
        return ResponseEntity.ok(count);
    }
    
    @Operation(summary = "统计不同状态数据集数量")
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countByStatus(
            @Parameter(description = "数据集状态") @PathVariable Integer status) {
        long count = datasetService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
}