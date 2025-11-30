package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.RegisterRequest;
import com.ai.cockpit.dto.request.UserRequest;
import com.ai.cockpit.dto.response.UserResponse;
import com.ai.cockpit.entity.Permission;
import com.ai.cockpit.entity.RolePermission;
import com.ai.cockpit.entity.User;
import com.ai.cockpit.entity.UserRole;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.UserMapper;
import com.ai.cockpit.repository.PermissionRepository;
import com.ai.cockpit.repository.RolePermissionRepository;
import com.ai.cockpit.repository.RoleRepository;
import com.ai.cockpit.repository.UserRepository;
import com.ai.cockpit.repository.UserRoleRepository;
import com.ai.cockpit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw BusinessException.userAlreadyExists();
        }

        // 检查邮箱是否已存在
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(10005, "邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStatus(1); // 启用状态
        user.setLastLoginTime(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        
        // 为用户分配默认角色（USER）
        roleRepository.findByCode("USER").ifPresent(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(savedUser.getId());
            userRole.setRoleId(role.getId());
            userRoleRepository.save(userRole);
        });
        
        log.info("用户注册成功: {}", savedUser.getUsername());
        
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(BusinessException::userNotFound);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(BusinessException::userNotFound);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(BusinessException::userNotFound);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(BusinessException::userNotFound);
        
        // 更新用户信息
        userMapper.updateEntity(request, existingUser);
        existingUser.setUpdatedTime(LocalDateTime.now());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("更新用户信息成功: {}", updatedUser.getUsername());
        
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(BusinessException::userNotFound);
        userRepository.delete(user);
        log.info("删除用户: {}", user.getUsername());
    }

    @Override
    public List<UserResponse> findAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    @Override
    public List<UserResponse> findEnabledUsers() {
        List<User> users = userRepository.findByStatus(1);
        return userMapper.toResponseList(users);
    }

    @Override
    public List<UserResponse> findByRoleId(Long roleId) {
        // 临时解决方案：通过用户角色关联表查询用户
        // List<User> users = userRepository.findByRoleId(roleId);
        
        List<UserRole> userRoles = userRoleRepository.findByRoleId(roleId);
        List<Long> userIds = userRoles.stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toList());
        
        List<User> users = userRepository.findAllById(userIds);
        return userMapper.toResponseList(users);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(BusinessException::userNotFound);
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw BusinessException.passwordError();
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        log.info("用户修改密码成功: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(BusinessException::userNotFound);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        log.info("重置用户密码: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(BusinessException::userNotFound);
        user.setStatus(enabled ? 1 : 0);
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);
        log.info("{}用户: {}", enabled ? "启用" : "禁用", user.getUsername());
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(BusinessException::userNotFound);
        
        // 删除用户现有的角色关联
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
        
        log.info("为用户分配角色: {}, 角色ID: {}", user.getUsername(), roleIds);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(10004, "账号已禁用");
        }
        
        // 获取用户的权限列表
        // List<String> permissions = permissionRepository.findPermissionsByUserId(user.getId());
        
        // 临时解决方案：通过用户角色和角色权限关联表查询用户权限
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
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
        List<String> permissionCodes = permissions.stream()
                .map(Permission::getCode)
                .collect(Collectors.toList());
        
        // 构建UserDetails对象
        List<SimpleGrantedAuthority> authorities = permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}