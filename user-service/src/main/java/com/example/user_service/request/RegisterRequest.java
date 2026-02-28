package com.example.user_service.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String password;
    private String phone;
    private String code;
}