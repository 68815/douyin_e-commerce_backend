package com.example.user_service.request;

import lombok.Data;

@Data
public class UpdateRequest {
    private String userName;
    private String userEmail;
    private String userPhoneNumber;
    private String userPassword;
}
