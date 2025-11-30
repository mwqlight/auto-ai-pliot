package com.ai.cockpit.controller;

import com.ai.cockpit.dto.response.ApiResult;
import com.ai.cockpit.dto.request.PermissionRequest;
import com.ai.cockpit.dto.response.PermissionResponse;
import com.ai.cockpit.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 权限管理控制器
 */
@Slf4j
@Tag(name = "权限管理", description = "权限管理相关接口")
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "创建权限", description = "创建新的权限")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<PermissionResponse> createPermission(
            @Valid @RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.createPermission(request);
        return ApiResult.success(response);
    }

    @Operation(summary = "更新权限", description = "更新权限信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<PermissionResponse> updatePermission(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request) {
        PermissionResponse response = permissionService.updatePermission(id, request);
        return ApiResult.success(response);
    }

    @Operation(summary = "删除权限", description = "删除权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> deletePermission(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResult.success("删除权限成功");
    }

    @Operation(summary = "根据ID查询权限", description = "根据权限ID查询权限详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<PermissionResponse> getPermissionById(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        PermissionResponse response = permissionService.findById(id);
        return ApiResult.success(response);
    }

    @Operation(summary = "根据代码查询权限", description = "根据权限代码查询权限详情")
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<PermissionResponse> getPermissionByCode(
            @Parameter(description = "权限代码") @PathVariable String code) {
        PermissionResponse response = permissionService.findByCode(code);
        return ApiResult.success(response);
    }

    @Operation(summary = "查询所有权限", description = "查询所有权限列表")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> responses = permissionService.findAllPermissions();
        return ApiResult.success(responses);
    }

    @Operation(summary = "查询启用的权限", description = "查询所有启用的权限列表")
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<PermissionResponse>> getEnabledPermissions() {
        List<PermissionResponse> responses = permissionService.findEnabledPermissions();
        return ApiResult.success(responses);
    }

    @Operation(summary = "根据类型查询权限", description = "根据权限类型查询权限列表")
    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<PermissionResponse>> getPermissionsByType(
            @Parameter(description = "权限类型: 1-菜单, 2-按钮, 3-接口") @PathVariable Integer type) {
        List<PermissionResponse> responses = permissionService.findByType(type);
        return ApiResult.success(responses);
    }

    @Operation(summary = "根据父权限ID查询权限", description = "根据父权限ID查询权限列表")
    @GetMapping("/parent/{parentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<PermissionResponse>> getPermissionsByParent(
            @Parameter(description = "父权限ID") @PathVariable Long parentId) {
        List<PermissionResponse> responses = permissionService.findByParentId(parentId);
        return ApiResult.success(responses);
    }

    @Operation(summary = "查询角色的权限列表", description = "查询指定角色的权限列表")
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<PermissionResponse>> getPermissionsByRole(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<PermissionResponse> responses = permissionService.findByRoleId(roleId);
        return ApiResult.success(responses);
    }

    @Operation(summary = "查询用户的权限列表", description = "查询指定用户的权限列表")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ApiResult<List<PermissionResponse>> getPermissionsByUser(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<PermissionResponse> responses = permissionService.findByUserId(userId);
        return ApiResult.success(responses);
    }

    @Operation(summary = "切换权限状态", description = "启用或禁用权限")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> togglePermissionStatus(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @Parameter(description = "启用状态: true-启用, false-禁用") @RequestParam Boolean enabled) {
        permissionService.togglePermissionStatus(id, enabled);
        return ApiResult.success(enabled ? "启用权限成功" : "禁用权限成功");
    }

    @Operation(summary = "查询权限树", description = "查询权限树形结构")
    @GetMapping("/tree")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<PermissionResponse>> getPermissionTree() {
        List<PermissionResponse> responses = permissionService.findPermissionTree();
        return ApiResult.success(responses);
    }
}