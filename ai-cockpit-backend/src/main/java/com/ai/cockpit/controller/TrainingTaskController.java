package com.ai.cockpit.controller;

import com.ai.cockpit.dto.request.TrainingTaskRequest;
import com.ai.cockpit.dto.response.TrainingTaskResponse;
import com.ai.cockpit.service.TrainingTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 训练任务控制器
 */
@Tag(name = "训练任务管理", description = "训练任务的创建、查询、更新、删除等操作")
@RestController
@RequestMapping("/api/v1/training-tasks")
@RequiredArgsConstructor
public class TrainingTaskController {
    
    private final TrainingTaskService trainingTaskService;
    
    @Operation(summary = "创建训练任务")
    @PostMapping
    public ResponseEntity<TrainingTaskResponse> createTask(
            @Validated @RequestBody TrainingTaskRequest request) {
        TrainingTaskResponse response = trainingTaskService.createTask(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "更新训练任务")
    @PutMapping("/{id}")
    public ResponseEntity<TrainingTaskResponse> updateTask(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @Validated @RequestBody TrainingTaskRequest request) {
        TrainingTaskResponse response = trainingTaskService.updateTask(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "删除训练任务")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        trainingTaskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "根据ID查询训练任务")
    @GetMapping("/{id}")
    public ResponseEntity<TrainingTaskResponse> getTaskById(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        TrainingTaskResponse response = trainingTaskService.findById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "根据名称查询训练任务")
    @GetMapping("/name/{name}")
    public ResponseEntity<TrainingTaskResponse> getTaskByName(
            @Parameter(description = "任务名称") @PathVariable String name) {
        TrainingTaskResponse response = trainingTaskService.findByName(name);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "获取所有训练任务列表")
    @GetMapping
    public ResponseEntity<List<TrainingTaskResponse>> getAllTasks() {
        List<TrainingTaskResponse> responses = trainingTaskService.findAllTasks();
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据状态查询训练任务列表")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TrainingTaskResponse>> getTasksByStatus(
            @Parameter(description = "任务状态：0-等待中，1-运行中，2-已完成，3-失败，4-已取消") 
            @PathVariable Integer status) {
        List<TrainingTaskResponse> responses = trainingTaskService.findByStatus(status);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据模型ID查询训练任务列表")
    @GetMapping("/model/{modelId}")
    public ResponseEntity<List<TrainingTaskResponse>> getTasksByModelId(
            @Parameter(description = "模型ID") @PathVariable Long modelId) {
        List<TrainingTaskResponse> responses = trainingTaskService.findByModelId(modelId);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据数据集ID查询训练任务列表")
    @GetMapping("/dataset/{datasetId}")
    public ResponseEntity<List<TrainingTaskResponse>> getTasksByDatasetId(
            @Parameter(description = "数据集ID") @PathVariable Long datasetId) {
        List<TrainingTaskResponse> responses = trainingTaskService.findByDatasetId(datasetId);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "根据创建者ID查询训练任务列表")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<TrainingTaskResponse>> getTasksByCreatorId(
            @Parameter(description = "创建者ID") @PathVariable Long creatorId) {
        List<TrainingTaskResponse> responses = trainingTaskService.findByCreatorId(creatorId);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(summary = "启动训练任务")
    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        trainingTaskService.startTask(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "停止训练任务")
    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        trainingTaskService.stopTask(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "取消训练任务")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelTask(
            @Parameter(description = "任务ID") @PathVariable Long id) {
        trainingTaskService.cancelTask(id);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "更新训练进度")
    @PutMapping("/{id}/progress")
    public ResponseEntity<Void> updateProgress(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @RequestParam Integer progress,
            @RequestParam Integer currentEpoch) {
        trainingTaskService.updateProgress(id, progress, currentEpoch);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "更新训练指标")
    @PutMapping("/{id}/metrics")
    public ResponseEntity<Void> updateMetrics(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @RequestBody String metrics) {
        trainingTaskService.updateMetrics(id, metrics);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "更新训练状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @RequestParam Integer status) {
        trainingTaskService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @Operation(summary = "记录训练错误")
    @PostMapping("/{id}/error")
    public ResponseEntity<Void> recordError(
            @Parameter(description = "任务ID") @PathVariable Long id,
            @RequestBody String errorMessage) {
        trainingTaskService.recordError(id, errorMessage);
        return ResponseEntity.ok().build();
    }
}