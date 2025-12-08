package com.example.user_service.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.RegisterRequest;
import com.example.user_service.dto.UpdateRequest;
import com.example.user_service.entity.User;
import com.example.user_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.login(username, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new User());
        }
    }

    /**
     * 用户注册（邮箱）
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register/email")
    public ResponseEntity<User> registerByEmail(@RequestBody RegisterRequest registerRequest) {
        // 验证验证码
        if (userService.verifyCode(registerRequest.getEmail(), registerRequest.getVerificationCode())) {
            return ResponseEntity.badRequest().build();
        }
        
        User user = userService.createUserByEmail(
            registerRequest.getEmail(), 
            registerRequest.getPassword(), 
            registerRequest.getUsername()
        );
        return ResponseEntity.ok(user);
    }

    /**
     * 用户注册（手机）
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register/phone")
    public ResponseEntity<User> registerByPhone(@RequestBody RegisterRequest registerRequest) {
        // 验证验证码
        if (userService.verifyCode(registerRequest.getPhoneNumber(), registerRequest.getVerificationCode())) {
            return ResponseEntity.badRequest().build();
        }
        
        User user = userService.createUserByPhone(
            registerRequest.getPhoneNumber(), 
            registerRequest.getPassword(), 
            registerRequest.getUsername()
        );
        return ResponseEntity.ok(user);
    }

    /**
     * 第三方用户注册
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register/thirdparty")
    public ResponseEntity<User> registerThirdParty(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok().build();
    }

    /**
     * 发送验证码
     * @param emailOrPhone 目标（邮箱或手机号）
     * @param type 验证码类型
     * @return 发送结果
     */
    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String emailOrPhone, @RequestParam String type) {
        userService.sendVerificationCode(emailOrPhone);
        return ResponseEntity.ok("验证码已发送");
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<Long> logout() {
        Long userId = StpUtil.getLoginIdAsLong();        
        userService.logout(userId);
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Lax")
                .secure(false)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userId);
    }

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {

        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户列表
     * @return 用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<User>> getUserList() {
        // 简化实现，实际应分页查询
        return ResponseEntity.ok().build();
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新用户信息
     * @param updateRequest 更新请求
     * @return 更新结果
     */
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateUser(@RequestBody UpdateRequest updateRequest) {
        boolean success = userService.updateUser(updateRequest);
        return ResponseEntity.ok(success);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(true);
    }

    /**
     * 注销当前用户
     * @return 是否成功
     */
    @DeleteMapping("/write-off")
    public ResponseEntity<Boolean> writeOffCurrentUser() {
        boolean success = userService.writeOffCurrentUser();
        return ResponseEntity.ok(success);
    }
}