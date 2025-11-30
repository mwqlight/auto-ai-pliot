package com.ai.cockpit.controller;

import com.ai.cockpit.dto.response.ApiResult;
import com.ai.cockpit.dto.request.UserRequest;
import com.ai.cockpit.dto.response.UserResponse;
import com.ai.cockpit.service.UserService;
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
 * 用户管理控制器
 */
@Slf4j
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "查询所有用户", description = "查询所有用户列表")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<UserResponse>> getAllUsers() {
        List<UserResponse> responses = userService.findAllUsers();
        return ApiResult.success(responses);
    }

    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.findById(#id).username == authentication.name")
    public ApiResult<UserResponse> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserResponse response = userService.findById(id);
        return ApiResult.success(response);
    }

    @Operation(summary = "根据用户名查询用户", description = "根据用户名查询用户详情")
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ApiResult<UserResponse> getUserByUsername(
            @Parameter(description = "用户名") @PathVariable String username) {
        UserResponse response = userService.findByUsername(username);
        return ApiResult.success(response);
    }

    @Operation(summary = "根据邮箱查询用户", description = "根据邮箱查询用户详情")
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<UserResponse> getUserByEmail(
            @Parameter(description = "邮箱地址") @PathVariable String email) {
        UserResponse response = userService.findByEmail(email);
        return ApiResult.success(response);
    }

    @Operation(summary = "更新用户信息", description = "更新用户信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.findById(#id).username == authentication.name")
    public ApiResult<UserResponse> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ApiResult.success("更新成功", response);
    }

    @Operation(summary = "删除用户", description = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResult.success("删除成功");
    }

    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    @PostMapping("/{id}/change-password")
    @PreAuthorize("@userService.findById(#id).username == authentication.name")
    public ApiResult<String> changePassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "原密码") @RequestParam String oldPassword,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        userService.changePassword(id, oldPassword, newPassword);
        return ApiResult.success("密码修改成功");
    }

    @Operation(summary = "重置密码", description = "管理员重置用户密码")
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> resetPassword(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return ApiResult.success("密码重置成功");
    }

    @Operation(summary = "切换用户状态", description = "启用或禁用用户")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> toggleUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "启用状态: true-启用, false-禁用") @RequestParam Boolean enabled) {
        userService.toggleUserStatus(id, enabled);
        return ApiResult.success(enabled ? "用户已启用" : "用户已禁用");
    }

    @Operation(summary = "为用户分配角色", description = "为用户分配角色")
    @PostMapping("/{id}/assign-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<String> assignRoles(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        userService.assignRoles(id, roleIds);
        return ApiResult.success("角色分配成功");
    }

    @Operation(summary = "查询启用的用户", description = "查询所有启用的用户列表")
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<UserResponse>> getEnabledUsers() {
        List<UserResponse> responses = userService.findEnabledUsers();
        return ApiResult.success(responses);
    }

    @Operation(summary = "根据角色查询用户", description = "根据角色ID查询用户列表")
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResult<List<UserResponse>> getUsersByRole(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<UserResponse> responses = userService.findByRoleId(roleId);
        return ApiResult.success(responses);
    }
}