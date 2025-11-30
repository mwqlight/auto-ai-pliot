package com.ai.cockpit.repository;

import com.ai.cockpit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查询用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据状态查询用户列表
     */
    List<User> findByStatus(Integer status);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据角色名称查询用户列表
     */
    /*@Query("SELECT DISTINCT u FROM User u JOIN UserRole ur ON u.id = ur.userId JOIN Role r ON ur.roleId = r.id WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);*/

    /**
     * 统计用户数量
     */
    long countByStatus(Integer status);

    /**
     * 根据用户名模糊查询
     */
    List<User> findByUsernameContaining(String username);
    
    /**
     * 根据角色ID查询用户列表
     */
    /*@Query("SELECT DISTINCT u FROM User u JOIN UserRole ur ON u.id = ur.userId WHERE ur.roleId = :roleId")
    List<User> findByRoleId(@Param("roleId") Long roleId);*/
}