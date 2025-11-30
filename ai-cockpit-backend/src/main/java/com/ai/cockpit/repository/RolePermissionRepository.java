package com.ai.cockpit.repository;

import com.ai.cockpit.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关联数据访问接口
 */
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
    /**
     * 根据角色ID查找关联关系
     */
    List<RolePermission> findByRoleId(Long roleId);
    
    /**
     * 根据权限ID查找关联关系
     */
    List<RolePermission> findByPermissionId(Long permissionId);
    
    /**
     * 根据角色ID和权限ID查找关联关系
     */
    RolePermission findByRoleIdAndPermissionId(Long roleId, Long permissionId);
    
    /**
     * 根据角色ID删除所有权限关联
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据权限ID删除所有角色关联
     */
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permissionId = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
    
    /**
     * 检查角色是否拥有某个权限
     */
    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN true ELSE false END FROM RolePermission rp WHERE rp.roleId = :roleId AND rp.permissionId = :permissionId")
    boolean existsByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 检查权限是否被角色使用
     */
    boolean existsByPermissionId(Long permissionId);
}