package com.example.authentication_and_authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationAndAuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationAndAuthorizationApplication.class, args);
    }
}