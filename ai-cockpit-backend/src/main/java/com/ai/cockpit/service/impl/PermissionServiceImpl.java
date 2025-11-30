package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.PermissionRequest;
import com.ai.cockpit.dto.response.PermissionResponse;
import com.ai.cockpit.entity.Permission;
import com.ai.cockpit.entity.RolePermission;
import com.ai.cockpit.entity.UserRole;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.PermissionMapper;
import com.ai.cockpit.repository.PermissionRepository;
import com.ai.cockpit.repository.RolePermissionRepository;
import com.ai.cockpit.repository.UserRoleRepository;
import com.ai.cockpit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @Transactional
    public PermissionResponse createPermission(PermissionRequest request) {
        // 检查权限代码是否已存在
        if (permissionRepository.existsByCode(request.getCode())) {
            throw new BusinessException(10010, "权限代码已存在");
        }
        
        // 检查权限名称是否已存在
        if (permissionRepository.existsByName(request.getName())) {
            throw new BusinessException(10011, "权限名称已存在");
        }
        
        // 转换DTO为实体
        Permission permission = permissionMapper.toEntity(request);
        permission.setCreatedTime(LocalDateTime.now());
        permission.setUpdatedTime(LocalDateTime.now());
        
        Permission savedPermission = permissionRepository.save(permission);
        log.info("创建权限成功: {}", savedPermission.getName());
        
        return permissionMapper.toResponse(savedPermission);
    }

    @Override
    @Transactional
    public PermissionResponse updatePermission(Long id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10012, "权限不存在"));
        
        // 检查权限代码是否已存在（排除当前权限）
        if (permissionRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new BusinessException(10010, "权限代码已存在");
        }
        
        // 检查权限名称是否已存在（排除当前权限）
        if (permissionRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BusinessException(10011, "权限名称已存在");
        }
        
        // 更新权限信息
        permissionMapper.updateEntity(request, permission);
        permission.setUpdatedTime(LocalDateTime.now());
        
        Permission updatedPermission = permissionRepository.save(permission);
        log.info("更新权限成功: {}", updatedPermission.getName());
        
        return permissionMapper.toResponse(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10012, "权限不存在"));
        
        // 检查是否有子权限
        if (permissionRepository.countByParentId(id) > 0) {
            throw new BusinessException(10013, "存在子权限，无法删除");
        }
        
        // 检查是否有角色关联
        if (rolePermissionRepository.existsByPermissionId(id)) {
            throw new BusinessException(10014, "权限已被角色使用，无法删除");
        }
        
        permissionRepository.delete(permission);
        log.info("删除权限成功: {}", permission.getName());
    }

    @Override
    public PermissionResponse findById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10012, "权限不存在"));
        return permissionMapper.toResponse(permission);
    }

    @Override
    public PermissionResponse findByCode(String code) {
        Permission permission = permissionRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(10012, "权限不存在"));
        return permissionMapper.toResponse(permission);
    }

    @Override
    public List<PermissionResponse> findAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissionMapper.toResponseList(permissions);
    }

    @Override
    public List<PermissionResponse> findEnabledPermissions() {
        List<Permission> permissions = permissionRepository.findByEnabledTrue();
        return permissionMapper.toResponseList(permissions);
    }

    @Override
    public List<PermissionResponse> findByType(Integer type) {
        List<Permission> permissions = permissionRepository.findByType(String.valueOf(type));
        return permissionMapper.toResponseList(permissions);
    }

    @Override
    public List<PermissionResponse> findByParentId(Long parentId) {
        List<Permission> permissions = permissionRepository.findByParentId(parentId);
        return permissionMapper.toResponseList(permissions);
    }

    @Override
    public List<PermissionResponse> findByRoleId(Long roleId) {
        // 临时解决方案：通过角色权限关联表查询权限
        // List<Permission> permissions = permissionRepository.findByRoleId(roleId);
        
        List<Long> permissionIds = rolePermissionRepository.findByRoleId(roleId).stream()
                .map(rp -> rp.getPermissionId())
                .collect(Collectors.toList());
        
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        return permissionMapper.toResponseList(permissions);
    }

    @Override
    public List<PermissionResponse> findByUserId(Long userId) {
        // 临时解决方案：通过用户角色和角色权限关联表查询用户权限
        // List<Permission> permissions = permissionRepository.findByUserId(userId);
        
        // 获取用户角色
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        
        // 获取角色权限（使用循环替代findByRoleIdIn）
        List<Long> permissionIds = new ArrayList<>();
        for (Long roleId : roleIds) {
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
            permissionIds.addAll(rolePermissions.stream()
                    .map(RolePermission::getPermissionId)
                    .collect(Collectors.toList()));
        }
        
        // 去重
        permissionIds = permissionIds.stream().distinct().collect(Collectors.toList());
        
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        return permissions.stream()
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void togglePermissionStatus(Long id, boolean enabled) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10012, "权限不存在"));
        
        permission.setEnabled(enabled);
        permission.setUpdatedTime(LocalDateTime.now());
        
        permissionRepository.save(permission);
        log.info("{}权限: {}", enabled ? "启用" : "禁用", permission.getName());
    }

    @Override
    public List<PermissionResponse> findPermissionTree() {
        // 获取所有权限
        List<Permission> permissions = permissionRepository.findAll();
        
        // 构建权限树
        return buildPermissionTree(permissions, null);
    }

    /**
     * 递归构建权限树
     */
    private List<PermissionResponse> buildPermissionTree(List<Permission> permissions, Long parentId) {
        return permissions.stream()
                .filter(p -> (parentId == null && p.getParentId() == null) || 
                           (parentId != null && parentId.equals(p.getParentId())))
                .map(permission -> {
                    PermissionResponse response = permissionMapper.toResponse(permission);
                    // 递归设置子权限
                    List<PermissionResponse> children = buildPermissionTree(permissions, permission.getId());
                    response.setChildren(children.isEmpty() ? null : children);
                    return response;
                })
                .collect(Collectors.toList());
    }
}