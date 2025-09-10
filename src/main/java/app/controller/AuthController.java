package app.controller;

import app.entity.User;
import app.service.AuthService;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册、登出等认证相关操作
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, 
                                    @RequestParam String password) {
        return authService.login(username, password);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new java.util.HashMap<>();
        boolean success = authService.register(user);
        result.put("success", success);
        result.put("message", success ? "注册成功" : "用户名已存在");
        return result;
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        authService.logout();
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("message", "登出成功");
        return result;
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userInfo")
    public Map<String, Object> getUserInfo() {
        Map<String, Object> result = new java.util.HashMap<>();
        User user = authService.getCurrentUser();
        if (user != null) {
            result.put("success", true);
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "用户未登录");
        }
        return result;
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/checkLogin")
    public Map<String, Object> checkLogin() {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("isLogin", authService.isLogin());
        result.put("loginId", StpUtil.isLogin() ? StpUtil.getLoginId() : null);
        return result;
    }

    /**
     * 获取令牌信息
     */
    @GetMapping("/tokenInfo")
    public SaTokenInfo getTokenInfo() {
        return authService.getTokenInfo();
    }

    /**
     * 封禁用户（管理员功能）
     */
    @PostMapping("/ban/{userId}")
    public Map<String, Object> banUser(@PathVariable Long userId, 
                                      @RequestParam(defaultValue = "3600") long time) {
        authService.banUser(userId, time);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("message", "用户封禁成功");
        return result;
    }

    /**
     * 解封用户（管理员功能）
     */
    @PostMapping("/unban/{userId}")
    public Map<String, Object> unbanUser(@PathVariable Long userId) {
        authService.unbanUser(userId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("message", "用户解封成功");
        return result;
    }
}