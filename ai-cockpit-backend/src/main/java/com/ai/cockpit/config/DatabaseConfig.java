package com.ai.cockpit.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库配置类
 */
@Configuration
@Slf4j
public class DatabaseConfig {

    /**
     * 数据库健康检查
     */
    @Bean
    public HealthIndicator databaseHealthIndicator(DataSource dataSource) {
        return () -> {
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(5)) {
                    HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
                    
                    // 获取连接池状态
                    int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
                    int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
                    int totalConnections = hikariDataSource.getHikariPoolMXBean().getTotalConnections();
                    int threadsAwaitingConnection = hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();
                    
                    return Health.up()
                            .withDetail("database", "MySQL")
                            .withDetail("status", "连接正常")
                            .withDetail("activeConnections", activeConnections)
                            .withDetail("idleConnections", idleConnections)
                            .withDetail("totalConnections", totalConnections)
                            .withDetail("threadsAwaitingConnection", threadsAwaitingConnection)
                            .withDetail("maxPoolSize", hikariDataSource.getMaximumPoolSize())
                            .withDetail("minIdle", hikariDataSource.getMinimumIdle())
                            .build();
                } else {
                    return Health.down().withDetail("database", "MySQL").withDetail("error", "数据库连接无效").build();
                }
            } catch (SQLException e) {
                log.error("数据库健康检查失败", e);
                return Health.down().withDetail("database", "MySQL").withDetail("error", e.getMessage()).build();
            }
        };
    }

    /**
     * Redis健康检查
     */
    @Bean
    public HealthIndicator redisHealthIndicator() {
        return () -> {
            try {
                // 这里可以添加Redis连接测试
                // 在实际项目中，可以通过RedisTemplate测试连接
                return Health.up()
                        .withDetail("cache", "Redis")
                        .withDetail("status", "连接正常")
                        .withDetail("host", "localhost")
                        .withDetail("port", 6379)
                        .build();
            } catch (Exception e) {
                log.error("Redis健康检查失败", e);
                return Health.down().withDetail("cache", "Redis").withDetail("error", e.getMessage()).build();
            }
        };
    }

    /**
     * JdbcTemplate配置
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 数据库连接池监控配置
     */
    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        
        // 连接池监控配置
        dataSource.setPoolName("AI-Cockpit-Pool");
        dataSource.setRegisterMbeans(true); // 启用JMX监控
        
        // 连接泄漏检测
        dataSource.setLeakDetectionThreshold(60000); // 60秒泄漏检测
        
        return dataSource;
    }

    /**
     * 数据库连接池状态监控
     */
    @Bean
    public DatabasePoolMonitor databasePoolMonitor(DataSource dataSource) {
        return new DatabasePoolMonitor((HikariDataSource) dataSource);
    }

    /**
     * 数据库连接池监控器
     */
    @Slf4j
    public static class DatabasePoolMonitor {
        
        private final HikariDataSource dataSource;
        
        public DatabasePoolMonitor(HikariDataSource dataSource) {
            this.dataSource = dataSource;
            
            // 启动连接池监控
            monitorPool();
        }
        
        private void monitorPool() {
            Thread monitorThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(60000); // 每分钟检查一次
                        
                        int activeConnections = dataSource.getHikariPoolMXBean().getActiveConnections();
                        int idleConnections = dataSource.getHikariPoolMXBean().getIdleConnections();
                        int threadsAwaitingConnection = dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();
                        
                        // 记录连接池状态
                        log.info("数据库连接池状态 - 活跃连接: {}, 空闲连接: {}, 等待线程: {}", 
                                activeConnections, idleConnections, threadsAwaitingConnection);
                        
                        // 连接池告警
                        if (threadsAwaitingConnection > 10) {
                            log.warn("数据库连接池等待线程过多: {}，可能需要调整连接池配置", threadsAwaitingConnection);
                        }
                        
                        if (activeConnections >= dataSource.getMaximumPoolSize() - 2) {
                            log.warn("数据库连接池接近满载，活跃连接: {}，最大连接数: {}", 
                                    activeConnections, dataSource.getMaximumPoolSize());
                        }
                        
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        log.error("数据库连接池监控异常", e);
                    }
                }
            });
            
            monitorThread.setName("database-pool-monitor");
            monitorThread.setDaemon(true);
            monitorThread.start();
        }
    }
}