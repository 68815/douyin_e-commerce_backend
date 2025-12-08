package com.example.user_service.service;

import com.example.user_service.dto.UpdateRequest;
import com.example.user_service.entity.User;

import java.security.AuthProvider;

/**
 * <p>
 * 用户服务接口
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
public interface IUserService {
    
    /**
     * 通过邮箱创建用户
     * @param email 邮箱
     * @param password 密码
     * @param username 用户名
     * @return 用户对象
     */
    User createUserByEmail(String email, String password, String username);
    
    /**
     * 通过邮箱创建用户（无用户名）
     * @param email 邮箱
     * @param password 密码
     * @return 用户对象
     */
    User createUserByEmail(String email, String password);
    
    /**
     * 通过手机号创建用户
     * @param phone 手机号
     * @param password 密码
     * @param username 用户名
     * @return 用户对象
     */
    User createUserByPhone(String phone, String password, String username);
    
    /**
     * 通过手机号创建用户（无用户名）
     * @param phone 手机号
     * @param password 密码
     * @return 用户对象
     */
    User createUserByPhone(String phone, String password);
    
    /**
     * 用户登录
     *
     * @param username 标识符（用户名/邮箱/手机号）
     * @param password   密码
     * @return 用户对象
     */
    User login(String username, String password);
    
    /**
     * 用户登出
     * @param userId 用户ID
     */
    void logout(Long userId);
    
    /**
     * 删除用户
     * @param id 用户ID
     */
    void deleteUser(Long id);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否成功
     */
    boolean updateUser(UpdateRequest updateRequest);
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Long id);
    
    /**
     * 发送验证码
     *
     * @param emailOrPhone 目标（邮箱或手机号）
     */
    void sendVerificationCode(String emailOrPhone);
    
    /**
     * 验证验证码
     * @param emailOrPhone 目标（邮箱或手机号）
     * @param code 验证码
     * @return 是否验证成功
     */
    boolean verifyCode(String emailOrPhone, String code);

    /**
     * 注销当前用户
     * @return 是否成功
     */
    boolean writeOffCurrentUser();
    /**
     * 第三方用户注册
     * @param authProvider 认证提供者
     * @param openId 第三方开放ID
     * @param unionId 第三方联合ID
     * @param accessToken 访问令牌
     * @param username 用户名
     * @return 用户对象
     */
    User registerThirdPartyUser(AuthProvider authProvider, String openId, String unionId, String accessToken, String username);
}