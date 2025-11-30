package com.ai.cockpit.controller;

import com.ai.cockpit.dto.response.ApiResult;
import com.ai.cockpit.dto.request.RoleRequest;
import com.ai.cockpit.dto.response.RoleResponse;
import com.ai.cockpit.service.RoleService;
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
 * 角色管理控制器
 */
@Slf4j
@Tag(name = "角色管理", description = "角色管理相关接口")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "创建角色", description = "创建新的角色")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<RoleResponse> createRole(
            @Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.createRole(request);
        return ApiResult.success(response);
    }

    @Operation(summary = "更新角色", description = "更新角色信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<RoleResponse> updateRole(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Valid @RequestBody RoleRequest request) {
        RoleResponse response = roleService.updateRole(id, request);
        return ApiResult.success(response);
    }

    @Operation(summary = "删除角色", description = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> deleteRole(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResult.success("删除角色成功");
    }

    @Operation(summary = "根据ID查询角色", description = "根据角色ID查询角色详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<RoleResponse> getRoleById(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        RoleResponse response = roleService.findById(id);
        return ApiResult.success(response);
    }

    @Operation(summary = "根据代码查询角色", description = "根据角色代码查询角色详情")
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<RoleResponse> getRoleByCode(
            @Parameter(description = "角色代码") @PathVariable String code) {
        RoleResponse response = roleService.findByCode(code);
        return ApiResult.success(response);
    }

    @Operation(summary = "查询所有角色", description = "查询所有角色列表")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> responses = roleService.findAllRoles();
        return ApiResult.success(responses);
    }

    @Operation(summary = "查询启用的角色", description = "查询所有启用的角色列表")
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<RoleResponse>> getEnabledRoles() {
        List<RoleResponse> responses = roleService.findEnabledRoles();
        return ApiResult.success(responses);
    }

    @Operation(summary = "根据类型查询角色", description = "根据角色类型查询角色列表")
    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<RoleResponse>> getRolesByType(
            @Parameter(description = "角色类型: 1-系统角色, 2-业务角色") @PathVariable Integer type) {
        List<RoleResponse> responses = roleService.findByType(type);
        return ApiResult.success(responses);
    }

    @Operation(summary = "查询用户的角色", description = "查询指定用户的角色列表")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ApiResult<List<RoleResponse>> getRolesByUser(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<RoleResponse> responses = roleService.findByUserId(userId);
        return ApiResult.success(responses);
    }

    @Operation(summary = "切换角色状态", description = "启用或禁用角色")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> toggleRoleStatus(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "启用状态: true-启用, false-禁用") @RequestParam Boolean enabled) {
        roleService.toggleRoleStatus(id, enabled);
        return ApiResult.success(enabled ? "启用角色成功" : "禁用角色成功");
    }

    @Operation(summary = "为角色分配权限", description = "为指定角色分配权限")
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return ApiResult.success("分配权限成功");
    }

    @Operation(summary = "查询角色的权限", description = "查询指定角色的权限ID列表")
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<Long>> getRolePermissions(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissions(id);
        return ApiResult.success(permissionIds);
    }

    @Operation(summary = "为用户分配角色", description = "为指定用户分配角色")
    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> assignUserRoles(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        roleService.assignUserRoles(userId, roleIds);
        return ApiResult.success("分配角色成功");
    }

    @Operation(summary = "查询用户的角色", description = "查询指定用户的角色ID列表")
    @GetMapping("/user/{userId}/ids")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ApiResult<List<Long>> getUserRoles(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<Long> roleIds = roleService.getUserRoles(userId);
        return ApiResult.success(roleIds);
    }
}