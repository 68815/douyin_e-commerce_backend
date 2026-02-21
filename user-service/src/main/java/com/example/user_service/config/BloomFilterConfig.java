package com.example.user_service.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置类
 */
@Configuration
public class BloomFilterConfig {
    
    /**
     * 用户存在性布隆过滤器
     * 用于快速判断用户是否已存在（邮箱、手机号、用户名）
     */
    @Bean
    public RBloomFilter<String> userExistsBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("user:exists:bloom");
        // 初始化：预期插入100万个元素，误判率0.01（1%）
        bloomFilter.tryInit(1000000, 0.01);
        return bloomFilter;
    }
    
    /**
     * 黑名单用户布隆过滤器
     * 用于快速识别被封禁的用户
     */
    @Bean
    public RBloomFilter<String> blacklistedUsersBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("user:blacklist:bloom");
        // 初始化：预期插入10万个元素，误判率0.001（0.1%）
        bloomFilter.tryInit(100000, 0.001);
        return bloomFilter;
    }
}