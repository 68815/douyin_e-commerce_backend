package app.service.impl;

import app.entity.User;
import app.mapper.UserMapper;
import app.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.dev33.satoken.stp.StpUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  用户服务实现类
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean deleteUser(Long userId) {
        return removeById(userId);
    }

    public Map<String, Object> insert(@NotNull User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        boolean success = save(user);
        return Map.of("success", success, "message", success ? "注册成功" : "用户名已存在");
    }

    @Override
    public boolean updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);
        if (null == user) {
            return Map.of("success", false, "message", "用户不存在");
        }
        if (!user.getPasswordHash().equals(password)) {
            return Map.of("success", false, "message", "密码错误");
        }
        return Map.of("success", true, "message", "登录成功");
    }
}