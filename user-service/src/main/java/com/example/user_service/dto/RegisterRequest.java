package com.example.user_service.dto;

import lombok.Data;

import java.security.AuthProvider;

@Data
public class RegisterRequest {
    private AuthProvider authProvider;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String verificationCode;
    private String openId;
    private String unionId;
    private String accessToken;
}