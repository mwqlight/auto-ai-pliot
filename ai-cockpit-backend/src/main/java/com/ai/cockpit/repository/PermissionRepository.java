package com.ai.cockpit.repository;

import com.ai.cockpit.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限数据访问接口
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /**
     * 根据权限代码查找权限
     */
    Optional<Permission> findByCode(String code);
    
    /**
     * 根据权限名称查找权限
     */
    Optional<Permission> findByName(String name);
    
    /**
     * 根据权限类型查找权限列表
     */
    List<Permission> findByType(String type);
    
    /**
     * 根据父权限ID查找权限列表
     */
    List<Permission> findByParentId(Long parentId);
    
    /**
     * 查找启用的权限列表
     */
    List<Permission> findByEnabledTrue();
    
    /**
     * 根据角色ID查找权限列表
     */
    /*@Query("SELECT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId WHERE rp.roleId = :roleId AND p.enabled = true")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);*/
    
    /**
     * 根据用户ID查找权限列表
     */
    /*@Query("SELECT DISTINCT p FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId JOIN User u ON rp.roleId IN (SELECT ur.roleId FROM UserRole ur WHERE ur.userId = :userId) WHERE p.enabled = true")
    List<Permission> findByUserId(@Param("userId") Long userId);*/
    
    /**
     * 根据父权限ID统计子权限数量
     */
    int countByParentId(Long parentId);
    
    /**
     * 检查权限代码是否已存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查权限名称是否已存在
     */
    boolean existsByName(String name);
    
    /**
     * 检查权限代码是否已存在（排除指定ID）
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * 检查权限名称是否已存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);
    
    /**
     * 根据用户ID查找权限代码列表
     */
    /*@Query("SELECT DISTINCT p.code FROM Permission p JOIN RolePermission rp ON p.id = rp.permissionId JOIN UserRole ur ON rp.roleId = ur.roleId WHERE ur.userId = :userId AND p.enabled = true")
    List<String> findPermissionsByUserId(@Param("userId") Long userId);*/
}