package com.example.user_service.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 密码工具类测试
 */
@SpringBootTest
public class PasswordUtilTest {

    @Test
    public void testEncodePassword() {
        String rawPassword = "test123";
        String encodedPassword = PasswordUtil.encodePassword(rawPassword);
        
        // 验证加密后的密码不为空且与原密码不同
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$")); // BCrypt前缀
    }
    
    @Test
    public void testMatches() {
        String rawPassword = "test123";
        String encodedPassword = PasswordUtil.encodePassword(rawPassword);
        
        // 验证密码匹配
        assertTrue(PasswordUtil.matches(rawPassword, encodedPassword));
        assertFalse(PasswordUtil.matches("wrongpassword", encodedPassword));
    }
    
    @Test
    public void testPasswordStrength() {
        // 测试密码强度验证 - 符合要求的密码
        assertTrue(PasswordUtil.isPasswordStrong("Test123!"));
        assertTrue(PasswordUtil.isPasswordStrong("MyPassword@123"));
        assertTrue(PasswordUtil.isPasswordStrong("Abc123#$%"));
        
        // 测试不符合要求的密码
        assertFalse(PasswordUtil.isPasswordStrong("1234567")); // 长度不足
        assertFalse(PasswordUtil.isPasswordStrong("test1234")); // 缺少大写字母
        assertFalse(PasswordUtil.isPasswordStrong("TEST1234")); // 缺少小写字母
        assertFalse(PasswordUtil.isPasswordStrong("Test1234")); // 缺少特殊字符
        assertFalse(PasswordUtil.isPasswordStrong(""));
        assertFalse(PasswordUtil.isPasswordStrong(null));
    }
    
    @Test
    public void testEncodeNullPassword() {
        // 测试空密码异常
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.encodePassword(null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.encodePassword("");
        });
    }
}