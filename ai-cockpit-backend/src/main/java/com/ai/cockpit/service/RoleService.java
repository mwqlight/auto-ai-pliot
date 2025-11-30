package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.RoleRequest;
import com.ai.cockpit.dto.response.RoleResponse;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
    
    /**
     * 创建角色
     */
    RoleResponse createRole(RoleRequest request);
    
    /**
     * 更新角色
     */
    RoleResponse updateRole(Long id, RoleRequest request);
    
    /**
     * 根据ID删除角色
     */
    void deleteRole(Long id);
    
    /**
     * 根据ID查找角色
     */
    RoleResponse findById(Long id);
    
    /**
     * 根据角色代码查找角色
     */
    RoleResponse findByCode(String code);
    
    /**
     * 获取所有角色列表
     */
    List<RoleResponse> findAllRoles();
    
    /**
     * 获取启用的角色列表
     */
    List<RoleResponse> findEnabledRoles();
    
    /**
     * 根据类型查找角色列表
     */
    List<RoleResponse> findByType(Integer type);
    
    /**
     * 根据用户ID查找角色列表
     */
    List<RoleResponse> findByUserId(Long userId);
    
    /**
     * 切换角色启用状态
     */
    void toggleRoleStatus(Long id, boolean enabled);
    
    /**
     * 为角色分配权限
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 获取角色的权限列表
     */
    List<Long> getRolePermissions(Long roleId);
    
    /**
     * 为用户分配角色
     */
    void assignUserRoles(Long userId, List<Long> roleIds);
    
    /**
     * 获取用户的角色列表
     */
    List<Long> getUserRoles(Long userId);
}