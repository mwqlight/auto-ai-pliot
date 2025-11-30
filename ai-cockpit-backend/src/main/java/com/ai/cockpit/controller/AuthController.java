package com.ai.cockpit.controller;

import com.ai.cockpit.dto.response.ApiResult;
import com.ai.cockpit.dto.request.LoginRequest;
import com.ai.cockpit.dto.request.RegisterRequest;
import com.ai.cockpit.dto.request.UserRequest;
import com.ai.cockpit.dto.response.AuthResponse;
import com.ai.cockpit.dto.response.UserResponse;
import com.ai.cockpit.service.UserService;
import com.ai.cockpit.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ApiResult<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成token
        String accessToken = jwtUtil.generateToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());
        
        // 更新用户登录信息
        UserResponse user = userService.findByUsername(request.getUsername());
        
        // 构建响应
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(jwtUtil.getExpirationDateFromToken(accessToken).getTime() / 1000);
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        response.setUserInfo(userInfo);
        
        log.info("用户登录成功: {}", request.getUsername());
        return ApiResult.success(response);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public ApiResult<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        log.info("用户注册成功: {}", user.getUsername());
        return ApiResult.success("注册成功", user);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新token", description = "刷新访问令牌")
    public ApiResult<AuthResponse> refreshToken(
            @Parameter(description = "刷新令牌") @RequestHeader("Authorization") String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return ApiResult.error(401, "无效的刷新token");
        }
        
        String token = refreshToken.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ApiResult.error(401, "刷新token已过期或无效");
        }
        
        String username = jwtUtil.getUsernameFromToken(token);
        String newAccessToken = jwtUtil.generateToken(username);
        
        AuthResponse response = new AuthResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(token); // 刷新token不变
        response.setExpiresIn(jwtUtil.getExpirationDateFromToken(newAccessToken).getTime() / 1000);
        
        return ApiResult.success(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public ApiResult<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();
            log.info("用户登出: {}", authentication.getName());
        }
        return ApiResult.success("登出成功");
    }

    @GetMapping("/profile")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    public ApiResult<AuthResponse.UserInfo> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ApiResult.error(401, "未认证");
        }
        
        UserResponse user = userService.findByUsername(authentication.getName());
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        
        return ApiResult.success(userInfo);
    }
}