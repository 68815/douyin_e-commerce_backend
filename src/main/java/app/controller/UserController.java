package app.controller;

import app.entity.User;
import app.service.IUserService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  用户控制器
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
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, 
                                    @RequestParam String password) {
        Map<String, Object> result = userService.login(username, password);
        if((boolean)result.get("success")){
            StpUtil.login(username);
        }
        return result;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        return userService.insert(user);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @SaCheckLogin
    public Map<String, Object> logout() {
        StpUtil.logout();
        return Map.of("success", true, "message", "登出成功");
    }

    /**
     * 获取当前用户信息（需要登录）
     */
    @GetMapping("/current")
    @SaCheckLogin
    public Map<String, Object> getCurrentUser() {
        User user = userService.getById(StpUtil.getLoginIdAsLong());
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("user", user);
        return result;
    }

    /**
     * 获取用户列表（需要登录）
     */
    @GetMapping("/list")
    @SaCheckLogin
    public List<User> listUsers() {
        return userService.list();
    }

    /**
     * 根据ID获取用户信息（需要登录）
     */
    @GetMapping("/{id}")
    @SaCheckLogin
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 更新用户信息（需要登录）
     */
    @PutMapping("/{id}")
    @SaCheckLogin
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean success = userService.updateById(user);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    /**
     * 删除用户（需要登录）
     */
    @DeleteMapping("/{id}")
    @SaCheckLogin
    @SaCheckRole("admin")
    @SaCheckPermission("user:delete")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}