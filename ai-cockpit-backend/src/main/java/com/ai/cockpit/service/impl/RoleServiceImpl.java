package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.RoleRequest;
import com.ai.cockpit.dto.response.RoleResponse;
import com.ai.cockpit.entity.Role;
import com.ai.cockpit.entity.RolePermission;
import com.ai.cockpit.entity.UserRole;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.RoleMapper;
import com.ai.cockpit.repository.RolePermissionRepository;
import com.ai.cockpit.repository.RoleRepository;
import com.ai.cockpit.repository.UserRoleRepository;
import com.ai.cockpit.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        // 检查角色代码是否已存在
        if (roleRepository.existsByCode(request.getCode())) {
            throw new BusinessException(10015, "角色代码已存在");
        }
        
        // 检查角色名称是否已存在
        if (roleRepository.existsByName(request.getName())) {
            throw new BusinessException(10016, "角色名称已存在");
        }
        
        // 转换DTO为实体
        Role role = roleMapper.toEntity(request);
        role.setCreatedTime(LocalDateTime.now());
        role.setUpdatedTime(LocalDateTime.now());
        
        Role savedRole = roleRepository.save(role);
        
        // 分配权限
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            assignPermissions(savedRole.getId(), request.getPermissionIds());
        }
        
        log.info("创建角色成功: {}", savedRole.getName());
        
        return roleMapper.toResponse(savedRole);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10017, "角色不存在"));
        
        // 检查角色代码是否已存在（排除当前角色）
        Optional<Role> existingRoleByCode = roleRepository.findByCode(request.getCode());
        if (existingRoleByCode.isPresent() && !existingRoleByCode.get().getId().equals(id)) {
            throw new BusinessException(10015, "角色代码已存在");
        }
        
        // 检查角色名称是否已存在（排除当前角色）
        Optional<Role> existingRoleByName = roleRepository.findByName(request.getName());
        if (existingRoleByName.isPresent() && !existingRoleByName.get().getId().equals(id)) {
            throw new BusinessException(10016, "角色名称已存在");
        }
        
        // 更新角色信息
        roleMapper.updateEntity(request, role);
        role.setUpdatedTime(LocalDateTime.now());
        
        Role updatedRole = roleRepository.save(role);
        
        // 更新权限分配
        if (request.getPermissionIds() != null) {
            assignPermissions(id, request.getPermissionIds());
        }
        
        log.info("更新角色成功: {}", updatedRole.getName());
        
        return roleMapper.toResponse(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10017, "角色不存在"));
        
        // 检查是否有用户关联此角色
        if (userRoleRepository.countByRoleId(id) > 0) {
            throw new BusinessException(10018, "角色已被用户使用，无法删除");
        }
        
        // 删除角色权限关联
        rolePermissionRepository.deleteByRoleId(id);
        
        // 删除角色
        roleRepository.delete(role);
        log.info("删除角色成功: {}", role.getName());
    }

    @Override
    public RoleResponse findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10017, "角色不存在"));
        return roleMapper.toResponse(role);
    }

    @Override
    public RoleResponse findByCode(String code) {
        Role role = roleRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(10017, "角色不存在"));
        return roleMapper.toResponse(role);
    }

    @Override
    public List<RoleResponse> findAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toResponseList(roles);
    }

    @Override
    public List<RoleResponse> findEnabledRoles() {
        List<Role> roles = roleRepository.findByEnabledTrue();
        return roleMapper.toResponseList(roles);
    }

    @Override
    public List<RoleResponse> findByType(Integer type) {
        List<Role> roles = roleRepository.findByType(String.valueOf(type));
        return roleMapper.toResponseList(roles);
    }

    @Override
    public List<RoleResponse> findByUserId(Long userId) {
        // 临时解决方案：通过用户角色关联表查询用户角色
        // List<Role> roles = roleRepository.findByUserId(userId);
        
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        
        List<Role> roles = roleRepository.findAllById(roleIds);
        return roles.stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void toggleRoleStatus(Long id, boolean enabled) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(10017, "角色不存在"));
        
        role.setEnabled(enabled);
        role.setUpdatedTime(LocalDateTime.now());
        
        roleRepository.save(role);
        log.info("{}角色: {}", enabled ? "启用" : "禁用", role.getName());
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(10017, "角色不存在"));
        
        // 删除现有权限关联
        rolePermissionRepository.deleteByRoleId(roleId);
        
        // 添加新的权限关联
        permissionIds.forEach(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionRepository.save(rolePermission);
        });
        
        log.info("为角色分配权限: {}, 权限数量: {}", role.getName(), permissionIds.size());
    }

    @Override
    public List<Long> getRolePermissions(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId).stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignUserRoles(Long userId, List<Long> roleIds) {
        // 删除用户现有角色关联
        List<UserRole> existingUserRoles = userRoleRepository.findByUserId(userId);
        for (UserRole userRole : existingUserRoles) {
            userRoleRepository.delete(userRole);
        }
        
        // 添加新的角色关联
        roleIds.forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleRepository.save(userRole);
        });
        
        log.info("为用户分配角色: 用户ID: {}, 角色数量: {}", userId, roleIds.size());
    }

    @Override
    public List<Long> getUserRoles(Long userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }
}