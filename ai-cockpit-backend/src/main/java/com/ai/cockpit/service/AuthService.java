package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.LoginRequest;
import com.ai.cockpit.dto.response.AuthResponse;
import com.ai.cockpit.dto.response.UserResponse;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     */
    AuthResponse login(LoginRequest loginRequest);
    
    /**
     * 用户登出
     */
    void logout(String token);
    
    /**
     * 刷新token
     */
    AuthResponse refreshToken(String refreshToken);
    
    /**
     * 获取当前用户信息
     */
    UserResponse getCurrentUser();
    
    /**
     * 验证token
     */
    boolean validateToken(String token);
    
    /**
     * 修改密码
     */
    void changePassword(String oldPassword, String newPassword);
}