package app.service.impl;

import app.entity.User;
import app.mapper.UserMapper;
import app.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean deleteUser(Long userId) {
        return this.removeById(userId);
    }

    @Override
    public boolean updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return this.getOne(queryWrapper);
    }
}