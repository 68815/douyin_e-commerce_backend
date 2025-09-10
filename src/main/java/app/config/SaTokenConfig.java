package app.config;

import app.util.AuthConfigUtil;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.context.SaHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * SaToken 配置类
 * 配置认证拦截器和动态白名单/黑名单
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @PostConstruct
    public void init() {
        // 初始化时可以添加一些默认的白名单和黑名单配置
        // 这些配置也可以在运行时通过AuthConfigController动态管理
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，使用动态的白名单和黑名单配置
        registry.addInterceptor(new SaInterceptor(handler -> {
            
            // 获取当前请求路径
            String path = SaHolder.getRequest().getRequestPath();
            
            // 检查白名单 - 使用动态配置
            List<String> whiteList = AuthConfigUtil.getWhiteList();
            for (String pattern : whiteList) {
                if (path.equals(pattern) || path.startsWith(pattern.replace("**", ""))) {
                    SaRouter.stop(); // 白名单路径直接放行
                    return;
                }
            }
            
            // 检查黑名单前缀 - 使用动态配置
            List<String> blackListPrefix = AuthConfigUtil.getBlackListPrefix();
            for (String prefix : blackListPrefix) {
                if (path.startsWith(prefix)) {
                    // 黑名单路径需要特殊权限校验（这里可以添加管理员权限校验）
                    StpUtil.checkLogin();
                    return;
                }
            }
            
            // 其他所有路径都需要登录校验
            StpUtil.checkLogin();
            
        })).addPathPatterns("/**");
    }
}