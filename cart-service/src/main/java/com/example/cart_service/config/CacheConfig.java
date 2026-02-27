package com.example.cart_service.config;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Redisson配置类
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@Configuration
public class CacheConfig {
    
    // RedissonClient bean 会由 redisson-spring-boot-starter 自动配置
    // 这里只需要确保能注入使用即可
    
}