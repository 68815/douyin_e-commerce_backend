package app.util;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 认证配置工具类
 * 支持动态管理白名单和黑名单
 */
@Component
public class AuthConfigUtil {

    // 动态白名单
    private static final CopyOnWriteArrayList<String> DYNAMIC_WHITE_LIST = new CopyOnWriteArrayList<>();
    
    // 动态黑名单
    private static final CopyOnWriteArrayList<String> DYNAMIC_BLACK_LIST = new CopyOnWriteArrayList<>();

    @Value("${auth.white-list:}")
    private List<String> staticWhiteList;

    @Value("${auth.black-list.prefix:}")
    private List<String> staticBlackListPrefix;

    @PostConstruct
    public void init() {
        // 初始化静态白名单
        if (staticWhiteList != null) {
            DYNAMIC_WHITE_LIST.addAll(staticWhiteList);
        }
        
        // 初始化静态黑名单前缀
        if (staticBlackListPrefix != null) {
            DYNAMIC_BLACK_LIST.addAll(staticBlackListPrefix);
        }
    }

    /**
     * 添加白名单路径
     */
    public static void addWhiteList(String path) {
        if (!DYNAMIC_WHITE_LIST.contains(path)) {
            DYNAMIC_WHITE_LIST.add(path);
        }
    }

    /**
     * 移除白名单路径
     */
    public static void removeWhiteList(String path) {
        DYNAMIC_WHITE_LIST.remove(path);
    }

    /**
     * 获取所有白名单路径
     */
    public static List<String> getWhiteList() {
        return new ArrayList<>(DYNAMIC_WHITE_LIST);
    }

    /**
     * 添加黑名单路径前缀
     */
    public static void addBlackListPrefix(String prefix) {
        if (!DYNAMIC_BLACK_LIST.contains(prefix)) {
            DYNAMIC_BLACK_LIST.add(prefix);
        }
    }

    /**
     * 移除黑名单路径前缀
     */
    public static void removeBlackListPrefix(String prefix) {
        DYNAMIC_BLACK_LIST.remove(prefix);
    }

    /**
     * 获取所有黑名单路径前缀
     */
    public static List<String> getBlackListPrefix() {
        return new ArrayList<>(DYNAMIC_BLACK_LIST);
    }

    /**
     * 检查路径是否在白名单中
     */
    public static boolean isWhiteList(String path) {
        return DYNAMIC_WHITE_LIST.stream().anyMatch(pattern -> 
            path.equals(pattern) || path.startsWith(pattern.replace("**", ""))
        );
    }

    /**
     * 检查路径是否在黑名单中
     */
    public static boolean isBlackList(String path) {
        return DYNAMIC_BLACK_LIST.stream().anyMatch(path::startsWith);
    }

    /**
     * 封禁用户（添加到黑名单）
     */
    public static void addUserToBlacklist(Long userId, long seconds) {
        StpUtil.disable(userId, seconds);
    }

    /**
     * 从黑名单中移除用户
     */
    public static void removeUserFromBlacklist(Long userId) {
        StpUtil.untieDisable(userId);
    }

    /**
     * 检查用户是否在黑名单中
     */
    public static boolean isUserInBlacklist(Long userId) {
        return StpUtil.isDisable(userId);
    }
}