package com.example.user_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String password;
    private String phone;
    private String code;
}