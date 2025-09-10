package app.controller;

import app.util.AuthConfigUtil;
import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 认证配置管理控制器
 * 支持动态管理白名单和黑名单（需要管理员权限）
 */
@RestController
@RequestMapping("/admin/auth-config")
@SaCheckRole("admin") // 需要管理员角色
public class AuthConfigController {

    /**
     * 获取所有白名单路径
     */
    @GetMapping("/white-list")
    public List<String> getWhiteList() {
        return AuthConfigUtil.getWhiteList();
    }

    /**
     * 添加白名单路径
     */
    @PostMapping("/white-list")
    public Map<String, Object> addWhiteList(@RequestParam String path) {
        AuthConfigUtil.addWhiteList(path);
        return Map.of("success", true, "message", "白名单添加成功");
    }

    /**
     * 移除白名单路径
     */
    @DeleteMapping("/white-list")
    public Map<String, Object> removeWhiteList(@RequestParam String path) {
        AuthConfigUtil.removeWhiteList(path);
        return Map.of("success", true, "message", "白名单移除成功");
    }

    /**
     * 获取所有黑名单路径前缀
     */
    @GetMapping("/black-list/prefix")
    public List<String> getBlackListPrefix() {
        return AuthConfigUtil.getBlackListPrefix();
    }

    /**
     * 添加黑名单路径前缀
     */
    @PostMapping("/black-list/prefix")
    public Map<String, Object> addBlackListPrefix(@RequestParam String prefix) {
        AuthConfigUtil.addBlackListPrefix(prefix);
        return Map.of("success", true, "message", "黑名单前缀添加成功");
    }

    /**
     * 移除黑名单路径前缀
     */
    @DeleteMapping("/black-list/prefix")
    public Map<String, Object> removeBlackListPrefix(@RequestParam String prefix) {
        AuthConfigUtil.removeBlackListPrefix(prefix);
        return Map.of("success", true, "message", "黑名单前缀移除成功");
    }

    /**
     * 检查路径是否在白名单中（支持正则表达式模式）
     * 正则表达式模式以^开头，例如：^/api/v\d+/.*$
     */
    @GetMapping("/check-white-list")
    public Map<String, Object> checkWhiteList(@RequestParam String path) {
        boolean isWhiteList = AuthConfigUtil.isWhiteList(path);
        return Map.of("isWhiteList", isWhiteList, "path", path);
    }

    /**
     * 检查路径是否在黑名单中
     */
    @GetMapping("/check-black-list")
    public Map<String, Object> checkBlackList(@RequestParam String path) {
        boolean isBlackList = AuthConfigUtil.isBlackList(path);
        return Map.of("isBlackList", isBlackList, "path", path);
    }

    /**
     * 封禁用户（添加到用户黑名单）
     */
    @PostMapping("/user-blacklist/{userId}")
    public Map<String, Object> banUser(@PathVariable Long userId, 
                                      @RequestParam(defaultValue = "3600") long seconds) {
        AuthConfigUtil.addUserToBlacklist(userId, seconds);
        return Map.of("success", true, "message", "用户封禁成功", "userId", userId, "seconds", seconds);
    }

    /**
     * 解封用户（从用户黑名单移除）
     */
    @DeleteMapping("/user-blacklist/{userId}")
    public Map<String, Object> unbanUser(@PathVariable Long userId) {
        AuthConfigUtil.removeUserFromBlacklist(userId);
        return Map.of("success", true, "message", "用户解封成功", "userId", userId);
    }

    /**
     * 检查用户是否在黑名单中
     */
    @GetMapping("/user-blacklist/check/{userId}")
    public Map<String, Object> checkUserBlacklist(@PathVariable Long userId) {
        boolean isBanned = AuthConfigUtil.isUserInBlacklist(userId);
        return Map.of("isBanned", isBanned, "userId", userId);
    }
}