package com.ai.cockpit.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:ai-cockpit-secret-key-2024}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800}")
    private Long refreshExpiration;

    /**
     * 生成JWT token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return createToken(claims, username, expiration);
    }

    /**
     * 生成JWT token（带自定义claims）
     */
    public String generateToken(Map<String, Object> claims, String username) {
        return createToken(claims, username, expiration);
    }

    /**
     * 生成刷新token
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("type", "refresh");
        return createToken(claims, username, refreshExpiration);
    }

    /**
     * 创建token
     */
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("从token中获取用户名失败", e);
            return null;
        }
    }

    /**
     * 验证token
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token已过期", e);
        } catch (UnsupportedJwtException e) {
            log.error("不支持的Token格式", e);
        } catch (MalformedJwtException e) {
            log.error("Token格式错误", e);
        } catch (SignatureException e) {
            log.error("Token签名验证失败", e);
        } catch (Exception e) {
            log.error("Token验证失败", e);
        }
        return false;
    }

    /**
     * 获取token过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("获取token过期时间失败", e);
            return null;
        }
    }

    /**
     * 判断token是否即将过期（在指定时间内）
     */
    public boolean isTokenExpiringSoon(String token, long minutes) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration == null) {
            return true;
        }
        
        long currentTime = System.currentTimeMillis();
        long expirationTime = expiration.getTime();
        long threshold = minutes * 60 * 1000; // 转换为毫秒
        
        return (expirationTime - currentTime) <= threshold;
    }

    /**
     * 获取token过期时间（秒）
     */
    public Long getExpiration() {
        return expiration;
    }
}