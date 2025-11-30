package com.ai.cockpit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * AI驾驶舱平台启动类
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class AiCockpitApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCockpitApplication.class, args);
    }
}