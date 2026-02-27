package com.example.user_service.client;

import com.example.user_service.mq.CartMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 购物车服务降级处理
 * </p>
 *
 * @author 0109
 * @since 2026-02-27
 */
@Slf4j
@Component
public class CartServiceFallback implements CartServiceClient {
    

    private final CartMessageHandler cartMessageHandler;
    @Autowired
    public CartServiceFallback(CartMessageHandler cartMessageHandler) {
        this.cartMessageHandler = cartMessageHandler;
    }
    
    @Override
    public boolean createCartForUser(Long userId) {
        log.warn("购物车服务调用失败，用户ID: {}，执行降级处理并通过消息队列重试", userId);

        //cartMessageHandler.sendCartCreateMessage(userId);
        
        return false; // 立即返回，不阻塞主流程
    }
}