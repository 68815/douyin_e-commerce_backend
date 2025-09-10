package app.service;

import app.entity.User;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;

import java.util.Map;

/**
 * 用户认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    Map<String, Object> login(String username, String password);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    User getCurrentUser();

    /**
     * 检查用户是否登录
     * @return 是否登录
     */
    boolean isLogin();

    /**
     * 获取令牌信息
     * @return 令牌信息
     */
    SaTokenInfo getTokenInfo();

    /**
     * 封禁用户
     * @param userId 用户ID
     * @param time 封禁时间（秒）
     */
    void banUser(Long userId, long time);

    /**
     * 解封用户
     * @param userId 用户ID
     */
    void unbanUser(Long userId);
}