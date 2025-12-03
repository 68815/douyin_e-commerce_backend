package com.example.authentication_and_authorization;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationAndAuthorizationApplication {

    public static void main(String[] args) {
        // 加载.env文件中的环境变量
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .ignoreIfMalformed()
                .load();
        
        // 将.env文件中的变量设置到系统环境中
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        
        SpringApplication.run(AuthenticationAndAuthorizationApplication.class, args);
    }
}