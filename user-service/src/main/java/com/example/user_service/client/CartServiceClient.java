package com.example.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 购物车服务Feign客户端
 * </p>
 *
 * @author 0109
 * @since 2026-02-27
 */
@FeignClient(name = "cart-service", fallback = CartServiceFallback.class)
public interface CartServiceClient {
    
    /**
     * 为新用户创建购物车
     * @param userId 用户ID
     * @return 是否创建成功
     */
    @PostMapping("/cart/create")
    boolean createCartForUser(@RequestParam("userId") Long userId);
}