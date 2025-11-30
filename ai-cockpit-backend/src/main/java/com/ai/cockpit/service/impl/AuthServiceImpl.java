package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.LoginRequest;
import com.ai.cockpit.dto.response.AuthResponse;
import com.ai.cockpit.dto.response.UserResponse;
import com.ai.cockpit.entity.User;
import com.ai.cockpit.mapper.UserMapper;
import com.ai.cockpit.repository.UserRepository;
import com.ai.cockpit.service.AuthService;
import com.ai.cockpit.util.JwtUtil;
import com.ai.cockpit.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 获取用户信息
            User user = (User) authentication.getPrincipal();
            
            // 生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("username", user.getUsername());
            claims.put("roles", user.getRoles());
            
            String accessToken = jwtUtil.generateToken(claims, user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
            
            // 更新用户登录信息
            user.setLastLoginTime(LocalDateTime.now());
            // 这里应该设置真实的IP地址
            user.setLastLoginIp("127.0.0.1");
            userRepository.save(user);
            
            // 缓存token
            redisUtil.set("token:" + user.getUsername(), accessToken, 24 * 60 * 60); // 24小时
            redisUtil.set("refresh_token:" + user.getUsername(), refreshToken, 7 * 24 * 60 * 60); // 7天
            
            // 构建响应
            AuthResponse response = new AuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setExpiresIn(jwtUtil.getExpiration());
            
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setEmail(user.getEmail());
            userInfo.setPhone(user.getPhone());
            userInfo.setRoles(user.getRoles().toArray(new String[0]));
            // 这里应该设置权限信息
            userInfo.setPermissions(new String[]{"user:read", "user:write"});
            
            response.setUserInfo(userInfo);
            
            log.info("用户登录成功: {}", user.getUsername());
            return response;
            
        } catch (Exception e) {
            log.error("用户登录失败: {}", loginRequest.getUsername(), e);
            throw new RuntimeException("用户名或密码错误");
        }
    }
    
    @Override
    public void logout(String token) {
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            redisUtil.delete("token:" + username);
            redisUtil.delete("refresh_token:" + username);
            SecurityContextHolder.clearContext();
            log.info("用户登出成功: {}", username);
        } catch (Exception e) {
            log.error("用户登出失败", e);
        }
    }
    
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            
            // 验证refresh token
            if (!jwtUtil.validateToken(refreshToken) || 
                !redisUtil.hasKey("refresh_token:" + username)) {
                throw new RuntimeException("无效的刷新token");
            }
            
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 生成新的token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("username", user.getUsername());
            claims.put("roles", user.getRoles());
            
            String newAccessToken = jwtUtil.generateToken(claims, username);
            String newRefreshToken = jwtUtil.generateRefreshToken(username);
            
            // 更新缓存
            redisUtil.set("token:" + username, newAccessToken, 24 * 60 * 60);
            redisUtil.set("refresh_token:" + username, newRefreshToken, 7 * 24 * 60 * 60);
            
            AuthResponse response = new AuthResponse();
            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken);
            response.setExpiresIn(jwtUtil.getExpiration());
            
            log.info("token刷新成功: {}", username);
            return response;
            
        } catch (Exception e) {
            log.error("token刷新失败", e);
            throw new RuntimeException("token刷新失败");
        }
    }
    
    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return userMapper.toResponse(user);
        }
        return null;
    }
    
    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
    
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        UserResponse currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        
        User user = userRepository.findById(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("用户密码修改成功: {}", user.getUsername());
    }
}