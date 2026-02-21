package com.example.user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 布隆过滤器服务类
 * 提供用户相关的布隆过滤器操作
 */
@Slf4j
@Service
public class UserBloomFilterService {

    private final RBloomFilter<String> userExistsBloomFilter;

    private final RBloomFilter<String> blacklistedUsersBloomFilter;

    @Autowired
    public UserBloomFilterService(RBloomFilter<String> userExistsBloomFilter, RBloomFilter<String> blacklistedUsersBloomFilter) {
        this.userExistsBloomFilter = userExistsBloomFilter;
        this.blacklistedUsersBloomFilter = blacklistedUsersBloomFilter;
    }
    
    /**
     * 添加用户标识到存在性过滤器
     * @param identifier 用户标识（邮箱/手机号/用户名）
     */
    public void addUserIdentifier(String identifier) {
        boolean added = userExistsBloomFilter.add(identifier);
        if (added) {
            log.info("[BLOOM_FILTER] 添加用户标识到存在性过滤器: {}", identifier);
        }
    }
    
    /**
     * 批量添加用户标识
     * @param identifiers 用户标识数组
     */
    public void addBatchUserIdentifiers(String... identifiers) {
        for (String identifier : identifiers) {
            addUserIdentifier(identifier);
        }
    }
    
    /**
     * 检查用户是否存在（快速预检查）
     * @param identifier 用户标识
     * @return 可能存在返回true，肯定不存在返回false
     */
    public boolean mightContainUser(String identifier) {
        boolean result = userExistsBloomFilter.contains(identifier);
        if (result) {
            log.debug("[BLOOM_FILTER] 用户标识可能存在: {}", identifier);
        }
        return result;
    }
    
    /**
     * 添加用户到黑名单
     * @param userId 用户ID
     */
    public void addToBlacklist(String userId) {
        boolean added = blacklistedUsersBloomFilter.add(userId);
        if (added) {
            log.info("[BLOOM_FILTER] 用户添加到黑名单: {}", userId);
        }
    }
    
    /**
     * 检查用户是否在黑名单中
     * @param userId 用户ID
     * @return 可能在黑名单返回true，肯定不在返回false
     */
    public boolean isUserBlacklisted(String userId) {
        boolean result = blacklistedUsersBloomFilter.contains(userId);
        if (result) {
            log.warn("[BLOOM_FILTER] 检测到黑名单用户: {}", userId);
        }
        return result;
    }
    
    /**
     * 获取布隆过滤器统计信息
     */
    public void printStatistics() {
        log.info("[BLOOM_FILTER] 用户存在性过滤器统计:");
        log.info("  预期插入数量: {}", userExistsBloomFilter.getExpectedInsertions());
        log.info("  哈希函数数量: {}", userExistsBloomFilter.getHashIterations());
        log.info("  误判率: {}", userExistsBloomFilter.getFalseProbability());
        
        log.info("[BLOOM_FILTER] 黑名单过滤器统计:");
        log.info("  预期插入数量: {}", blacklistedUsersBloomFilter.getExpectedInsertions());
        log.info("  哈希函数数量: {}", blacklistedUsersBloomFilter.getHashIterations());
        log.info("  误判率: {}", blacklistedUsersBloomFilter.getFalseProbability());
    }
}