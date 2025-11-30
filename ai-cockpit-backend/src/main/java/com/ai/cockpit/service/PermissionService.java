package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.PermissionRequest;
import com.ai.cockpit.dto.response.PermissionResponse;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    /**
     * 创建权限
     */
    PermissionResponse createPermission(PermissionRequest request);
    
    /**
     * 更新权限
     */
    PermissionResponse updatePermission(Long id, PermissionRequest request);
    
    /**
     * 根据ID删除权限
     */
    void deletePermission(Long id);
    
    /**
     * 根据ID查找权限
     */
    PermissionResponse findById(Long id);
    
    /**
     * 根据权限代码查找权限
     */
    PermissionResponse findByCode(String code);
    
    /**
     * 获取所有权限列表
     */
    List<PermissionResponse> findAllPermissions();
    
    /**
     * 获取启用的权限列表
     */
    List<PermissionResponse> findEnabledPermissions();
    
    /**
     * 根据类型查找权限列表
     */
    List<PermissionResponse> findByType(Integer type);
    
    /**
     * 根据父权限ID查找权限列表
     */
    List<PermissionResponse> findByParentId(Long parentId);
    
    /**
     * 根据角色ID查找权限列表
     */
    List<PermissionResponse> findByRoleId(Long roleId);
    
    /**
     * 根据用户ID查找权限列表
     */
    List<PermissionResponse> findByUserId(Long userId);
    
    /**
     * 切换权限启用状态
     */
    void togglePermissionStatus(Long id, boolean enabled);

    /**
     * 获取权限树结构
     */
    List<PermissionResponse> findPermissionTree();
}