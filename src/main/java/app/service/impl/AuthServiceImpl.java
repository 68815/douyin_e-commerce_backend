package app.service.impl;

import app.entity.User;
import app.mapper.UserMapper;
import app.service.AuthService;
import app.service.IUserService;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.exception.NotPermissionException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();

        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userService.getOne(queryWrapper);

        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }

        // 检查密码
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            result.put("success", false);
            result.put("message", "密码错误");
            return result;
        }

        // 检查用户是否被封禁
        if (StpUtil.isDisable(user.getId())) {
            result.put("success", false);
            result.put("message", "账号已被封禁");
            return result;
        }

        // 登录成功
        StpUtil.login(user.getId());
        
        // 更新最后登录时间
        user.setUpdatedAt(LocalDateTime.now());
        userService.updateById(user);

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("token", tokenInfo.getTokenValue());
        result.put("userId", user.getId());
        result.put("username", user.getUsername());

        return result;
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (userService.count(queryWrapper) > 0) {
            return false;
        }

        // 加密密码
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userService.save(user);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public User getCurrentUser() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        long userId = StpUtil.getLoginIdAsLong();
        return userService.getById(userId);
    }

    @Override
    public boolean isLogin() {
        return StpUtil.isLogin();
    }

    @Override
    public SaTokenInfo getTokenInfo() {
        return StpUtil.getTokenInfo();
    }

    @Override
    public void banUser(Long userId, long time) {
        // 检查当前用户是否有封禁其他用户的权限
        if (!StpUtil.hasPermission("user:ban")) {
            throw new NotPermissionException("user:ban", "没有封禁用户的权限");
        }
        StpUtil.disable(userId, time);
    }

    @Override
    public void unbanUser(Long userId) {
        // 检查当前用户是否有解封用户的权限
        if (!StpUtil.hasPermission("user:unban")) {
            throw new NotPermissionException("user:unban", "没有解封用户的权限");
        }
        
        StpUtil.untieDisable(userId);
    }
}