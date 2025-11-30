package com.ai.cockpit.repository;

import com.ai.cockpit.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联数据访问接口
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    /**
     * 根据用户ID查找关联关系
     */
    List<UserRole> findByUserId(Long userId);
    
    /**
     * 根据角色ID查找关联关系
     */
    List<UserRole> findByRoleId(Long roleId);
    
    /**
     * 根据用户ID和角色ID查找关联关系
     */
    UserRole findByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * 根据用户ID删除所有角色关联
     */
    /*@Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);*/
    
    /**
     * 根据角色ID删除所有用户关联
     */
    /*@Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Long roleId);*/
    
    /**
     * 检查用户是否拥有某个角色
     */
    /*@Query("SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.userId = :userId AND ur.roleId = :roleId")
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);*/
    
    /**
     * 统计角色下的用户数量
     */
    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.roleId = :roleId")
    long countByRoleId(@Param("roleId") Long roleId);
}