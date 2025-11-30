package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.RegisterRequest;
import com.ai.cockpit.dto.request.UserRequest;
import com.ai.cockpit.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends UserDetailsService {

    /**
     * 用户注册
     */
    UserResponse register(RegisterRequest request);

    /**
     * 根据用户名查询用户
     */
    UserResponse findByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    UserResponse findByEmail(String email);

    /**
     * 根据ID查询用户
     */
    UserResponse findById(Long id);

    /**
     * 更新用户信息
     */
    UserResponse updateUser(Long id, UserRequest request);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 查询所有用户
     */
    List<UserResponse> findAllUsers();

    /**
     * 查询所有启用的用户
     */
    List<UserResponse> findEnabledUsers();

    /**
     * 根据角色ID查询用户
     */
    List<UserResponse> findByRoleId(Long roleId);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 启用/禁用用户
     */
    void toggleUserStatus(Long userId, boolean enabled);

    /**
     * 分配角色
     */
    void assignRoles(Long userId, List<Long> roleIds);
}