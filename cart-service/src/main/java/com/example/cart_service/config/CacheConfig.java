package com.example.cart_service.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Redisson缓存配置类
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    /**
     * Redisson缓存管理器配置
     */
    @Bean
    public RedissonCacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        
        // 购物车缓存配置 - 30分钟过期
        config.put("cart", new CacheConfig(30 * 60 * 1000, 30 * 60 * 1000));
        
        // 默认缓存配置 - 10分钟过期
        config.put("default", new CacheConfig(10 * 60 * 1000, 10 * 60 * 1000));
        
        return new RedissonCacheManager(redissonClient, config);
    }
}