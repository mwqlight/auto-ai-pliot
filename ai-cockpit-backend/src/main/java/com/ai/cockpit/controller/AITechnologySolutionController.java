package com.ai.cockpit.controller;

import com.ai.cockpit.dto.request.AITechnologySolutionRequest;
import com.ai.cockpit.dto.response.AITechnologySolutionResponse;
import com.ai.cockpit.service.AITechnologySolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * AI技术方案控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai-technology-solutions")
@RequiredArgsConstructor
@Tag(name = "AI技术方案管理", description = "AI技术方案中心管理接口")
public class AITechnologySolutionController {
    
    private final AITechnologySolutionService solutionService;
    
    @PostMapping
    @Operation(summary = "创建AI技术方案", description = "创建新的AI技术方案")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public ResponseEntity<AITechnologySolutionResponse> createSolution(
            @Validated @RequestBody AITechnologySolutionRequest request,
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        log.info("创建AI技术方案，创建者ID：{}", userId);
        AITechnologySolutionResponse response = solutionService.createSolution(request, userId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新AI技术方案", description = "根据ID更新AI技术方案")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public ResponseEntity<AITechnologySolutionResponse> updateSolution(
            @PathVariable Long id,
            @Validated @RequestBody AITechnologySolutionRequest request) {
        log.info("更新AI技术方案，ID：{}", id);
        AITechnologySolutionResponse response = solutionService.updateSolution(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除AI技术方案", description = "根据ID删除AI技术方案")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public ResponseEntity<Void> deleteSolution(@PathVariable Long id) {
        log.info("删除AI技术方案，ID：{}", id);
        solutionService.deleteSolution(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询AI技术方案", description = "根据ID获取AI技术方案详情")
    public ResponseEntity<AITechnologySolutionResponse> getSolutionById(@PathVariable Long id) {
        log.debug("根据ID查询AI技术方案，ID：{}", id);
        AITechnologySolutionResponse response = solutionService.getSolutionById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "根据代码查询AI技术方案", description = "根据方案代码获取AI技术方案详情")
    public ResponseEntity<AITechnologySolutionResponse> getSolutionByCode(@PathVariable String code) {
        log.debug("根据代码查询AI技术方案，代码：{}", code);
        AITechnologySolutionResponse response = solutionService.getSolutionByCode(code);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "分页查询所有AI技术方案", description = "获取所有AI技术方案的分页列表")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getAllSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sort) {
        log.debug("分页查询所有AI技术方案，页码：{}，大小：{}，排序：{}", page, size, sort);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        Page<AITechnologySolutionResponse> response = solutionService.getAllSolutions(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/technology-type/{technologyType}")
    @Operation(summary = "根据技术类型查询", description = "根据技术类型查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByTechnologyType(
            @PathVariable String technologyType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据技术类型查询AI技术方案，技术类型：{}", technologyType);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByTechnologyType(technologyType, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/algorithm-type/{algorithmType}")
    @Operation(summary = "根据算法类型查询", description = "根据算法类型查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByAlgorithmType(
            @PathVariable String algorithmType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据算法类型查询AI技术方案，算法类型：{}", algorithmType);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByAlgorithmType(algorithmType, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/application-domain/{applicationDomain}")
    @Operation(summary = "根据适用领域查询", description = "根据适用领域查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByApplicationDomain(
            @PathVariable String applicationDomain,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据适用领域查询AI技术方案，适用领域：{}", applicationDomain);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByApplicationDomain(applicationDomain, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询", description = "根据状态查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByStatus(
            @PathVariable Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据状态查询AI技术方案，状态：{}", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByStatus(status, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/public/{isPublic}")
    @Operation(summary = "根据是否公开查询", description = "根据是否公开查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByIsPublic(
            @PathVariable Integer isPublic,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据是否公开查询AI技术方案，是否公开：{}", isPublic);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByIsPublic(isPublic, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索AI技术方案", description = "根据关键词搜索AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> searchSolutions(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("搜索AI技术方案，关键词：{}，状态：{}", keyword, status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.searchSolutions(keyword, status, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/filter")
    @Operation(summary = "多条件筛选查询", description = "根据多个条件组合筛选AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByMultipleCriteria(
            @RequestParam(required = false) String technologyType,
            @RequestParam(required = false) String algorithmType,
            @RequestParam(required = false) String applicationDomain,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer isPublic,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("多条件筛选查询AI技术方案，技术类型：{}，算法类型：{}，适用领域：{}，状态：{}，是否公开：{}", 
                technologyType, algorithmType, applicationDomain, status, isPublic);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByMultipleCriteria(
                technologyType, algorithmType, applicationDomain, status, isPublic, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/tag/{tag}")
    @Operation(summary = "根据标签查询", description = "根据标签查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据标签查询AI技术方案，标签：{}", tag);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByTag(tag, pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "查询热门方案", description = "查询使用次数最多的AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getPopularSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("查询热门AI技术方案");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "usageCount"));
        Page<AITechnologySolutionResponse> response = solutionService.getPopularSolutions(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/top-rated")
    @Operation(summary = "查询评分最高方案", description = "查询评分最高的AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getTopRatedSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("查询评分最高的AI技术方案");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "rating"));
        Page<AITechnologySolutionResponse> response = solutionService.getTopRatedSolutions(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/latest")
    @Operation(summary = "查询最新方案", description = "查询最新发布的AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getLatestSolutions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("查询最新发布的AI技术方案");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getLatestSolutions(pageable);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/creator/{creatorId}")
    @Operation(summary = "根据创建者查询", description = "根据创建者ID查询AI技术方案")
    public ResponseEntity<Page<AITechnologySolutionResponse>> getSolutionsByCreator(
            @PathVariable Long creatorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("根据创建者查询AI技术方案，创建者ID：{}", creatorId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AITechnologySolutionResponse> response = solutionService.getSolutionsByCreator(creatorId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/publish")
    @Operation(summary = "发布方案", description = "发布AI技术方案")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public ResponseEntity<AITechnologySolutionResponse> publishSolution(@PathVariable Long id) {
        log.info("发布AI技术方案，ID：{}", id);
        AITechnologySolutionResponse response = solutionService.publishSolution(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/unpublish")
    @Operation(summary = "下线方案", description = "下线AI技术方案")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public ResponseEntity<AITechnologySolutionResponse> unpublishSolution(@PathVariable Long id) {
        log.info("下线AI技术方案，ID：{}", id);
        AITechnologySolutionResponse response = solutionService.unpublishSolution(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/archive")
    @Operation(summary = "归档方案", description = "归档AI技术方案")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DEVELOPER')")
    public ResponseEntity<AITechnologySolutionResponse> archiveSolution(@PathVariable Long id) {
        log.info("归档AI技术方案，ID：{}", id);
        AITechnologySolutionResponse response = solutionService.archiveSolution(id);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/increment-usage")
    @Operation(summary = "增加使用次数", description = "增加AI技术方案的使用次数")
    public ResponseEntity<Void> incrementUsageCount(@PathVariable Long id) {
        log.debug("增加AI技术方案使用次数，ID：{}", id);
        solutionService.incrementUsageCount(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/rating")
    @Operation(summary = "更新评分", description = "更新AI技术方案的评分")
    public ResponseEntity<AITechnologySolutionResponse> updateRating(
            @PathVariable Long id,
            @RequestParam BigDecimal rating) {
        log.info("更新AI技术方案评分，ID：{}，评分：{}", id, rating);
        AITechnologySolutionResponse response = solutionService.updateRating(id, rating);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/statistics/technology-type")
    @Operation(summary = "技术类型统计", description = "统计各技术类型的方案数量")
    public ResponseEntity<List<Object[]>> countSolutionsByTechnologyType() {
        log.debug("统计各技术类型的方案数量");
        List<Object[]> statistics = solutionService.countSolutionsByTechnologyType();
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/statistics/status")
    @Operation(summary = "状态统计", description = "统计各状态的方案数量")
    public ResponseEntity<List<Object[]>> countSolutionsByStatus() {
        log.debug("统计各状态的方案数量");
        List<Object[]> statistics = solutionService.countSolutionsByStatus();
        return ResponseEntity.ok(statistics);
    }
}