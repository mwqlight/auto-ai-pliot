package com.ai.cockpit.controller;

import com.ai.cockpit.dto.request.BusinessApplicationRequest;
import com.ai.cockpit.dto.response.BusinessApplicationResponse;
import com.ai.cockpit.service.BusinessApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 业务应用市场控制器
 */
@RestController
@RequestMapping("/api/v1/business-applications")
@RequiredArgsConstructor
@Tag(name = "业务应用市场", description = "业务应用市场管理API")
public class BusinessApplicationController {
    
    private final BusinessApplicationService applicationService;
    
    @PostMapping
    @Operation(summary = "创建应用", description = "创建新的业务应用")
    @PreAuthorize("hasPermission('business_application:create')")
    public ResponseEntity<BusinessApplicationResponse> createApplication(
            @RequestBody BusinessApplicationRequest request) {
        BusinessApplicationResponse response = applicationService.createApplication(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新应用", description = "更新指定的业务应用")
    @PreAuthorize("hasPermission('business_application:update')")
    public ResponseEntity<BusinessApplicationResponse> updateApplication(
            @PathVariable Long id,
            @RequestBody BusinessApplicationRequest request) {
        BusinessApplicationResponse response = applicationService.updateApplication(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除应用", description = "删除指定的业务应用")
    @PreAuthorize("hasPermission('business_application:delete')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取应用详情", description = "根据ID获取应用详情")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<BusinessApplicationResponse> getApplicationById(@PathVariable Long id) {
        BusinessApplicationResponse response = applicationService.getApplicationById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "根据代码获取应用", description = "根据应用代码获取应用详情")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<BusinessApplicationResponse> getApplicationByCode(@PathVariable String code) {
        BusinessApplicationResponse response = applicationService.getApplicationByCode(code);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "分页查询所有应用", description = "分页查询所有业务应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getAllApplications(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "createdTime") String sort,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<BusinessApplicationResponse> responses = applicationService.getAllApplications(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索应用", description = "根据名称搜索应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> searchApplications(
            @Parameter(description = "应用名称") @RequestParam String name,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.searchApplicationsByName(name, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类查询应用", description = "根据分类查询应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getApplicationsByCategory(
            @PathVariable String category,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getApplicationsByCategory(category, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/category/{category}/sub-category/{subCategory}")
    @Operation(summary = "根据分类和子分类查询应用", description = "根据分类和子分类查询应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getApplicationsByCategoryAndSubCategory(
            @PathVariable String category,
            @PathVariable String subCategory,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getApplicationsByCategoryAndSubCategory(category, subCategory, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/free")
    @Operation(summary = "查询免费应用", description = "查询所有免费应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getFreeApplications(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getFreeApplications(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/recommended")
    @Operation(summary = "查询推荐应用", description = "查询所有推荐应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getRecommendedApplications(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getRecommendedApplications(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "查询热门应用", description = "查询所有热门应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getPopularApplications(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getPopularApplications(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/public")
    @Operation(summary = "查询公开应用", description = "查询所有公开应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getPublicApplications(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getPublicApplications(pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询应用", description = "根据状态查询应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getApplicationsByStatus(
            @PathVariable Integer status,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getApplicationsByStatus(status, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/creator/{creatorId}")
    @Operation(summary = "根据创建者查询应用", description = "根据创建者ID查询应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getApplicationsByCreator(
            @PathVariable Long creatorId,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getApplicationsByCreator(creatorId, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/tag/{tag}")
    @Operation(summary = "根据标签查询应用", description = "根据标签查询应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> getApplicationsByTag(
            @PathVariable String tag,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.getApplicationsByTag(tag, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/advanced-search")
    @Operation(summary = "高级搜索", description = "多条件搜索应用")
    @PreAuthorize("hasPermission('business_application:read')")
    public ResponseEntity<Page<BusinessApplicationResponse>> advancedSearch(
            @Parameter(description = "应用名称") @RequestParam(required = false) String name,
            @Parameter(description = "应用分类") @RequestParam(required = false) String category,
            @Parameter(description = "是否免费") @RequestParam(required = false) Boolean isFree,
            @Parameter(description = "应用状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<BusinessApplicationResponse> responses = applicationService.searchApplications(name, category, isFree, status, pageable);
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/{id}/publish")
    @Operation(summary = "发布应用", description = "发布指定的应用")
    @PreAuthorize("hasPermission('business_application:publish')")
    public ResponseEntity<BusinessApplicationResponse> publishApplication(@PathVariable Long id) {
        BusinessApplicationResponse response = applicationService.publishApplication(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/offline")
    @Operation(summary = "下线应用", description = "下线指定的应用")
    @PreAuthorize("hasPermission('business_application:offline')")
    public ResponseEntity<BusinessApplicationResponse> offlineApplication(@PathVariable Long id) {
        BusinessApplicationResponse response = applicationService.offlineApplication(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/archive")
    @Operation(summary = "归档应用", description = "归档指定的应用")
    @PreAuthorize("hasPermission('business_application:archive')")
    public ResponseEntity<BusinessApplicationResponse> archiveApplication(@PathVariable Long id) {
        BusinessApplicationResponse response = applicationService.archiveApplication(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/recommend")
    @Operation(summary = "设置推荐状态", description = "设置应用的推荐状态")
    @PreAuthorize("hasPermission('business_application:recommend')")
    public ResponseEntity<BusinessApplicationResponse> setRecommendation(
            @PathVariable Long id,
            @Parameter(description = "是否推荐") @RequestParam Boolean isRecommended) {
        BusinessApplicationResponse response = applicationService.setRecommendation(id, isRecommended);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/public")
    @Operation(summary = "设置公开状态", description = "设置应用的公开状态")
    @PreAuthorize("hasPermission('business_application:public')")
    public ResponseEntity<BusinessApplicationResponse> setPublicStatus(
            @PathVariable Long id,
            @Parameter(description = "是否公开") @RequestParam Boolean isPublic) {
        BusinessApplicationResponse response = applicationService.setPublicStatus(id, isPublic);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/usage")
    @Operation(summary = "增加使用次数", description = "增加应用的使用次数")
    @PreAuthorize("hasPermission('business_application:usage')")
    public ResponseEntity<Void> incrementUsageCount(@PathVariable Long id) {
        applicationService.incrementUsageCount(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/download")
    @Operation(summary = "增加下载次数", description = "增加应用的下载次数")
    @PreAuthorize("hasPermission('business_application:download')")
    public ResponseEntity<Void> incrementDownloadCount(@PathVariable Long id) {
        applicationService.incrementDownloadCount(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/favorite")
    @Operation(summary = "增加收藏次数", description = "增加应用的收藏次数")
    @PreAuthorize("hasPermission('business_application:favorite')")
    public ResponseEntity<Void> incrementFavoriteCount(@PathVariable Long id) {
        applicationService.incrementFavoriteCount(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/review")
    @Operation(summary = "增加评论次数", description = "增加应用的评论次数")
    @PreAuthorize("hasPermission('business_application:review')")
    public ResponseEntity<Void> incrementReviewCount(@PathVariable Long id) {
        applicationService.incrementReviewCount(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/rating")
    @Operation(summary = "更新评分", description = "更新应用的评分")
    @PreAuthorize("hasPermission('business_application:rating')")
    public ResponseEntity<BusinessApplicationResponse> updateRating(
            @PathVariable Long id,
            @Parameter(description = "评分") @RequestParam BigDecimal rating) {
        BusinessApplicationResponse response = applicationService.updateRating(id, rating);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/statistics/overview")
    @Operation(summary = "获取应用统计概览", description = "获取应用的整体统计信息")
    @PreAuthorize("hasPermission('business_application:statistics')")
    public ResponseEntity<Map<String, Object>> getApplicationStatistics() {
        Map<String, Object> statistics = applicationService.getApplicationStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/statistics/category")
    @Operation(summary = "获取分类统计", description = "获取各分类的应用数量统计")
    @PreAuthorize("hasPermission('business_application:statistics')")
    public ResponseEntity<Map<String, Long>> getCategoryStatistics() {
        Map<String, Long> statistics = applicationService.getCategoryStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/statistics/status")
    @Operation(summary = "获取状态统计", description = "获取各状态的应用数量统计")
    @PreAuthorize("hasPermission('business_application:statistics')")
    public ResponseEntity<Map<Integer, Long>> getStatusStatistics() {
        Map<Integer, Long> statistics = applicationService.getStatusStatistics();
        return ResponseEntity.ok(statistics);
    }
}