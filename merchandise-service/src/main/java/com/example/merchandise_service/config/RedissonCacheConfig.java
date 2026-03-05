package com.example.merchandise_service.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Redisson 缓存配置
 * 启用 Spring Cache 注解支持，使用 Redisson 作为底层实现
 * 
 * @author 0109
 * @since 2026-03-04
 */
@Configuration
@EnableCaching
public class RedissonCacheConfig {

    /**
     * 配置 RedissonSpringCacheManager
     * 定义各个缓存区域的配置（TTL=30 分钟，最大空闲时间=10 分钟）
     */
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        // 定义缓存配置
        Map<String, CacheConfig> config = new HashMap<>();
        
        // products 缓存：用于商品列表、分页等数据
        config.put("products", new CacheConfig(30 * 60 * 1000, 10 * 60 * 1000));
        
        // product 缓存：用于单个商品详情数据
        config.put("product", new CacheConfig(30 * 60 * 1000, 10 * 60 * 1000));
        
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
