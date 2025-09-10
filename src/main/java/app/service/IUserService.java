package app.service;

import app.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;



/**
 * <p>
 *  用户服务接口
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
public interface IUserService extends IService<User> {



    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    boolean deleteUser(Long userId);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updateUser(User user);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);
}