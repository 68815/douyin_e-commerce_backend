package com.example.authentication_and_authorization.config;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ServerWebExchange;

import java.util.Enumeration;

/**
 * [Sa-Token 权限认证] 配置类
 */
@Slf4j
@Configuration
public class SaTokenConfigure {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**") // 拦截全部path
                // 开放地址：用户登录、注册、发送验证码
                .addExclude("/user/login")
                .addExclude("/user/register")
                .addExclude("/user/sendVerificationCode")
                .setBeforeAuth(obj -> {
                    try {
                        ServerWebExchange exchange = SaReactorSyncHolder.getExchange();
                        if (exchange != null) {
                            ServerHttpRequest request = exchange.getRequest();
                            log.info("请求URL: {} {}", request.getMethod(), request.getURI());
                            log.info("请求头信息:");
                            request.getHeaders().forEach((name, values) ->
                                    log.info("  {}: {}", name, values));
                        } else {
                            log.debug("无法从SaReactorSyncHolder获取ServerWebExchange");
                        }
                    } catch (Exception e) {
                        log.warn("获取请求信息时发生异常: {}", e.getMessage());
                    }
                })
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    //进入鉴权操作后的所有路由都需要登录校验
                    SaRouter.match("/**", r -> StpUtil.checkLogin());

                    // 权限认证 -- 不同模块, 校验不同权限
                    /*SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
                    SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
                    SaRouter.match("/cart/**", r -> StpUtil.checkPermission("cart"));
                    SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));*/
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    log.error("认证错误: {}", e.getMessage());
                    return SaResult.error(e.getMessage());
                })
                ;
    }
}