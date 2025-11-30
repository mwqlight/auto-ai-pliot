package com.ai.cockpit.controller;

import com.ai.cockpit.dto.request.ModelRequest;
import com.ai.cockpit.dto.response.ApiResult;
import com.ai.cockpit.dto.response.ModelResponse;
import com.ai.cockpit.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模型控制器
 */
@RestController
@RequestMapping("/api/v1/models")
@RequiredArgsConstructor
@Tag(name = "模型管理", description = "模型生命周期管理接口")
public class ModelController {

    private final ModelService modelService;

    @PostMapping
    @Operation(summary = "创建模型", description = "创建新的模型记录")
    public ResponseEntity<ApiResult<ModelResponse>> createModel(@Validated @RequestBody ModelRequest request) {
        ModelResponse response = modelService.createModel(request);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新模型", description = "根据ID更新模型信息")
    public ResponseEntity<ApiResult<ModelResponse>> updateModel(
            @PathVariable Long id, 
            @Validated @RequestBody ModelRequest request) {
        ModelResponse response = modelService.updateModel(id, request);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模型", description = "根据ID删除模型")
    public ResponseEntity<ApiResult<Void>> deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
        return ResponseEntity.ok(ApiResult.success());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取模型详情", description = "根据ID获取模型详细信息")
    public ResponseEntity<ApiResult<ModelResponse>> getModelById(@PathVariable Long id) {
        ModelResponse response = modelService.findById(id);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "根据名称获取模型", description = "根据模型名称获取模型信息")
    public ResponseEntity<ApiResult<ModelResponse>> getModelByName(@PathVariable String name) {
        ModelResponse response = modelService.findByName(name);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    @GetMapping
    @Operation(summary = "获取所有模型", description = "获取所有模型列表")
    public ResponseEntity<ApiResult<List<ModelResponse>>> getAllModels() {
        List<ModelResponse> responses = modelService.findAllModels();
        return ResponseEntity.ok(ApiResult.success(responses));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取模型", description = "根据模型类型获取模型列表")
    public ResponseEntity<ApiResult<List<ModelResponse>>> getModelsByType(@PathVariable String type) {
        List<ModelResponse> responses = modelService.findByType(type);
        return ResponseEntity.ok(ApiResult.success(responses));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取模型", description = "根据模型状态获取模型列表")
    public ResponseEntity<ApiResult<List<ModelResponse>>> getModelsByStatus(@PathVariable Integer status) {
        List<ModelResponse> responses = modelService.findByStatus(status);
        return ResponseEntity.ok(ApiResult.success(responses));
    }

    @GetMapping("/creator/{creatorId}")
    @Operation(summary = "根据创建者获取模型", description = "根据创建者ID获取模型列表")
    public ResponseEntity<ApiResult<List<ModelResponse>>> getModelsByCreator(@PathVariable Long creatorId) {
        List<ModelResponse> responses = modelService.findByCreatorId(creatorId);
        return ResponseEntity.ok(ApiResult.success(responses));
    }

    @PutMapping("/{id}/public")
    @Operation(summary = "切换公开状态", description = "切换模型的公开状态")
    public ResponseEntity<ApiResult<Void>> togglePublicStatus(
            @PathVariable Long id, 
            @RequestParam Boolean isPublic) {
        modelService.toggleModelPublicStatus(id, isPublic);
        return ResponseEntity.ok(ApiResult.success());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新模型状态", description = "更新模型的训练/部署状态")
    public ResponseEntity<ApiResult<Void>> updateModelStatus(
            @PathVariable Long id, 
            @RequestParam Integer status) {
        modelService.updateModelStatus(id, status);
        return ResponseEntity.ok(ApiResult.success());
    }

    @PutMapping("/{id}/performance")
    @Operation(summary = "更新性能指标", description = "更新模型的性能指标")
    public ResponseEntity<ApiResult<Void>> updateModelPerformance(
            @PathVariable Long id, 
            @RequestBody String performanceMetrics) {
        modelService.updateModelPerformance(id, performanceMetrics);
        return ResponseEntity.ok(ApiResult.success());
    }

    @GetMapping("/search")
    @Operation(summary = "搜索模型", description = "根据关键词搜索模型")
    public ResponseEntity<ApiResult<List<ModelResponse>>> searchModels(@RequestParam String keyword) {
        List<ModelResponse> responses = modelService.searchModels(keyword);
        return ResponseEntity.ok(ApiResult.success(responses));
    }
}