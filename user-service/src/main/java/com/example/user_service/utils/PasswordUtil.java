package com.example.user_service.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具类
 * 提供密码的加密和验证功能
 */
@Component
public class PasswordUtil {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * 验证密码（兼容BCrypt和SHA-256格式）
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        
        // 如果是BCrypt格式（以$2a$开头），使用BCrypt验证
        if (encodedPassword.startsWith("$2a$")) {
            return encoder.matches(rawPassword, encodedPassword);
        }
        
        // 如果是SHA-256格式（包含冒号），使用SHA-256验证
        if (encodedPassword.contains(":")) {
            return verifySHA256Password(rawPassword, encodedPassword);
        }
        
        // 如果都不是，可能是明文密码（仅用于过渡期）
        return rawPassword.equals(encodedPassword);
    }
    
    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return encoder.encode(rawPassword);
    }
    
    /**
     * 验证SHA-256格式的密码
     * @param rawPassword 原始密码
     * @param storedPassword 存储的SHA-256密码（格式：hash:salt）
     * @return 是否匹配
     */
    private static boolean verifySHA256Password(String rawPassword, String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String storedHash = parts[0];
            String salt = parts[1];
            
            // 使用相同的盐重新计算哈希
            String computedHash = sha256Hash(rawPassword + salt);
            
            return storedHash.equals(computedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 计算SHA-256哈希
     * @param input 输入字符串
     * @return SHA-256哈希值
     */
    private static String sha256Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        
        // 转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * 检查密码强度
     * @param password 密码
     * @return 是否符合强度要求
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // 必须包含小写字母
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        // 必须包含大写字母
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // 必须包含特殊字符
        if (!password.matches(".*[^A-Za-z0-9].*")) {
            return false;
        }
        
        return true;
    }
}