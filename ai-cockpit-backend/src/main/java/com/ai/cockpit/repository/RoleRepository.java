package com.ai.cockpit.repository;

import com.ai.cockpit.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色数据访问接口
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * 根据角色代码查找角色
     */
    Optional<Role> findByCode(String code);
    
    /**
     * 根据角色名称查找角色
     */
    Optional<Role> findByName(String name);
    
    /**
     * 查找启用的角色列表
     */
    List<Role> findByEnabledTrue();
    
    /**
     * 根据角色类型查找角色列表
     */
    List<Role> findByType(String type);
    

    
    /**
     * 检查角色代码是否已存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查角色名称是否已存在
     */
    boolean existsByName(String name);
}