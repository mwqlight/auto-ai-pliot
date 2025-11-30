package com.ai.cockpit.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置类
 */
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    /**
     * 缓存管理器配置
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 默认缓存1小时
                .disableCachingNullValues() // 不缓存null值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        // 针对不同缓存设置不同的过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 用户相关缓存配置
        cacheConfigurations.put("user", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("user:token", defaultCacheConfig.entryTtl(Duration.ofDays(7)));
        cacheConfigurations.put("user:permission", defaultCacheConfig.entryTtl(Duration.ofHours(2)));
        
        // 模型相关缓存配置
        cacheConfigurations.put("model", defaultCacheConfig.entryTtl(Duration.ofHours(6)));
        cacheConfigurations.put("model:list", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("model:training", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));
        
        // 数据集相关缓存配置
        cacheConfigurations.put("dataset", defaultCacheConfig.entryTtl(Duration.ofHours(6)));
        cacheConfigurations.put("dataset:list", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        
        // 部署相关缓存配置
        cacheConfigurations.put("deployment", defaultCacheConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("deployment:status", defaultCacheConfig.entryTtl(Duration.ofMinutes(5)));
        
        // 系统配置缓存
        cacheConfigurations.put("system:config", defaultCacheConfig.entryTtl(Duration.ofDays(1)));
        
        // 验证码缓存
        cacheConfigurations.put("captcha", defaultCacheConfig.entryTtl(Duration.ofMinutes(5)));
        
        // API限流缓存
        cacheConfigurations.put("rate:limit", defaultCacheConfig.entryTtl(Duration.ofMinutes(1)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * 自定义缓存key生成器
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName());
            sb.append(":");
            sb.append(method.getName());
            for (Object obj : params) {
                if (obj != null) {
                    sb.append(":");
                    sb.append(obj.toString());
                }
            }
            return sb.toString();
        };
    }

    /**
     * 缓存异常处理
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
                // 缓存获取失败时，记录日志但不抛出异常，让业务继续执行
                System.err.println("缓存获取失败，key: " + key + ", 错误: " + exception.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException exception, org.springframework.cache.Cache cache, Object key, Object value) {
                // 缓存写入失败时，记录日志但不抛出异常
                System.err.println("缓存写入失败，key: " + key + ", 错误: " + exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
                // 缓存删除失败时，记录日志但不抛出异常
                System.err.println("缓存删除失败，key: " + key + ", 错误: " + exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, org.springframework.cache.Cache cache) {
                // 缓存清空失败时，记录日志但不抛出异常
                System.err.println("缓存清空失败，错误: " + exception.getMessage());
            }
        };
    }

    /**
     * 缓存解析器
     */
    @Override
    public CacheResolver cacheResolver() {
        return null; // 使用默认的缓存解析器
    }
}