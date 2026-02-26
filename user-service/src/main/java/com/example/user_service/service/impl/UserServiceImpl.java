package com.example.user_service.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user_service.dto.UpdateRequest;
import com.example.user_service.entity.User;
import com.example.user_service.entity.VerificationCode;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.mapper.VerificationCodeMapper;
import com.example.user_service.service.IUserService;
import com.example.user_service.service.UserBloomFilterService;
import com.example.user_service.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.AuthProvider;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private VerificationCodeMapper verificationCodeMapper;
    
    @Autowired
    private UserBloomFilterService userBloomFilterService;

    @Override
    public User createUserByEmail(String email, String password, String username) {
        // 使用布隆过滤器进行快速预检查
        if (userBloomFilterService.mightContainUser(email) ||
            userBloomFilterService.mightContainUser(username)) {
            log.warn(String.format("[USER_SERVICE] 布隆过滤器检测到可能存在的用户: email=%s, username=%s", email, username));
            return null;
        }
        // 密码强度验证
        if (!PasswordUtil.isPasswordStrong(password)) {
            throw new IllegalArgumentException("密码强度不符合要求");
        }

        User user = new User();
        user.setUserEmail(email);
        // 对密码进行加密存储
        user.setUserPassword(PasswordUtil.encodePassword(password));
        user.setUserName(username);
        user.setRegisterDateTime(LocalDateTime.now());
        user.setLatestLoginDateTime(LocalDateTime.now());
        user.setTotalLoginDays(1);
        user.setConsecutiveLoginDays(1);
        user.setTrustScore(100);

        userMapper.insert(user);

        // 将用户标识添加到布隆过滤器
        userBloomFilterService.addBatchUserIdentifiers(email, username);

        return user;
    }

    @Override
    public User createUserByEmail(String email, String password) {
        return createUserByEmail(email, password, "user_" + System.currentTimeMillis());
    }

    @Override
    public User createUserByPhone(String phone, String password, String username) {
        // 使用布隆过滤器进行快速预检查
        if (userBloomFilterService.mightContainUser(phone) ||
            userBloomFilterService.mightContainUser(username)) {
            log.warn(String.format("[USER_SERVICE] 布隆过滤器检测到可能存在的用户: phone=%s, username=%s", phone, username));
            return null;
        }

        // 密码强度验证
        if (!PasswordUtil.isPasswordStrong(password)) {
            throw new IllegalArgumentException("密码强度不符合要求");
        }

        User user = new User();
        user.setUserPhoneNumber(phone);
        // 对密码进行加密存储
        user.setUserPassword(PasswordUtil.encodePassword(password));
        user.setUserName(username);
        user.setRegisterDateTime(LocalDateTime.now());
        user.setLatestLoginDateTime(LocalDateTime.now());
        user.setTotalLoginDays(1);
        user.setConsecutiveLoginDays(1);
        user.setTrustScore(100);
        userMapper.insert(user);

        // 将用户标识添加到布隆过滤器
        userBloomFilterService.addBatchUserIdentifiers(phone, username);

        return user;
    }

    @Override
    public User createUserByPhone(String phone, String password) {
        return createUserByPhone(phone, password, "user_" + System.currentTimeMillis());
    }

    @Override
    public User login(String username, String password) {
        // 先检查是否在黑名单中
        if (userBloomFilterService.isUserBlacklisted(username)) {
            log.warn(String.format("[USER_SERVICE] 黑名单用户尝试登录: %s", username));
            return null; // 或者抛出特定异常
        }

        // 先查询用户（不包含密码条件）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq("user_email", username)
                   .or()
                   .eq("user_phone_number", username)
                   .or()
                   .eq("user_name", username));

        User user = userMapper.selectOne(queryWrapper);

        // 如果用户存在且密码验证通过
        if (user != null && PasswordUtil.matches(password, user.getUserPassword())) {
            if (user.getLatestLoginDateTime().toLocalDate().equals(LocalDateTime.now().toLocalDate().minusDays(1))) {
                user.setConsecutiveLoginDays(user.getConsecutiveLoginDays() + 1);
                user.setTotalLoginDays(user.getTotalLoginDays() + 1);
            } else if (!user.getLatestLoginDateTime().toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
                user.setTotalLoginDays(user.getTotalLoginDays() + 1);
                user.setConsecutiveLoginDays(1);
            }
            user.setLatestLoginDateTime(LocalDateTime.now());
            userMapper.updateById(user);
            StpUtil.login(user.getUserId());
            return user;
        }
        return null;
    }

    @Override
    public void logout(Long userId) {
        StpUtil.logout(userId);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public boolean updateUser(UpdateRequest updateRequest) {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (updateRequest.getUserName() != null) {
            user.setUserName(updateRequest.getUserName());
        }
        if (updateRequest.getUserEmail() != null) {
            user.setUserEmail(updateRequest.getUserEmail());
        }
        if (updateRequest.getUserPhoneNumber() != null) {
            user.setUserPhoneNumber(updateRequest.getUserPhoneNumber());
        }
        if (updateRequest.getUserPassword() != null) {
            // 更新密码时也进行加密
            user.setUserPassword(PasswordUtil.encodePassword(updateRequest.getUserPassword()));
        }
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public void sendVerificationCode(String emailOrPhone) {
        String code = String.format("%06d", new Random().nextInt(999999));

        if (sendCodeViaSMSOrEmail(emailOrPhone, code)) {
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmailOrPhone(emailOrPhone);
            verificationCode.setVerifyCode(code);
            verificationCode.setIsUsed(0);
            verificationCode.setCreatedAt(LocalDateTime.now());
            
            verificationCodeMapper.insert(verificationCode);
        } else {
        }
    }
    
    /**
     * 发送验证码的方法（模拟实现）
     * 实际项目中应该根据emailOrPhone判断是发送邮件还是短信
     * @param emailOrPhone 接收验证码的邮箱或手机号
     * @param code 验证码
     * @return 发送是否成功
     */
    private boolean sendCodeViaSMSOrEmail(String emailOrPhone, String code) {
        try {
            if (emailOrPhone.contains("@")) {

            } else {

            }
            return true;
        } catch (Exception e) {
            System.err.println("发送验证码时出现异常: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyCode(String emailOrPhone, String code) {
        QueryWrapper<VerificationCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email_or_phone", emailOrPhone)
                    .eq("verify_code", code)
                    .eq("is_used", 0);
        VerificationCode verificationCode = verificationCodeMapper.selectOne(queryWrapper);
        if (null == verificationCode) {
            return true;
        }
        verificationCode.setIsUsed(1);
        verificationCodeMapper.updateById(verificationCode);
        return false;
    }

    @Override
    public boolean writeOffCurrentUser() {
        StpUtil.logout();
        userMapper.deleteById(StpUtil.getLoginIdAsLong());
        return true;
    }

    @Override
    public User registerThirdPartyUser(AuthProvider authProvider, String openId, String unionId, String accessToken, String username) {
        
        return null;
    }
}