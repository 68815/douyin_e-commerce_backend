package com.example.user_service.mq;

import com.example.user_service.client.CartServiceClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 购物车创建消息队列处理器
 * 使用 RabbitMQ 实现异步重试
 * </p>
 *
 * @author 0109
 * @since 2026-02-27
 */
@Slf4j
@Component
public class CartMessageHandler {

    private final RabbitTemplate rabbitTemplate;

    private final CartServiceClient cartServiceClient;
    
    // 引用配置类中的常量
    public static final String CART_CREATE_EXCHANGE = "cart.create.exchange";
    public static final String CART_CREATE_QUEUE = "cart.create.queue";
    public static final String CART_CREATE_ROUTING_KEY = "cart.create.routing.key";

    @Autowired
    public CartMessageHandler(RabbitTemplate rabbitTemplate, CartServiceClient cartServiceClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.cartServiceClient = cartServiceClient;
    }
    
    /**
     * 发送购物车创建消息
     */
    public void sendCartCreateMessage(Long userId) {
        try {
            CartCreateMessage message = new CartCreateMessage();
            message.setUserId(userId);
            message.setRetryCount(0);
            message.setTimestamp(System.currentTimeMillis());
            
            rabbitTemplate.convertAndSend(
                CART_CREATE_EXCHANGE,
                CART_CREATE_ROUTING_KEY,
                message
            );
        } catch (Exception e) {
            log.error("发送购物车创建消息失败，用户ID: {}", userId, e);
        }
    }
    
    /**
     * 监听购物车创建队列
     */
    //@RabbitListener(queues = CART_CREATE_QUEUE)
    public void handleCartCreateMessage(CartCreateMessage message) {
        try {
            Long userId = message.getUserId();
            int retryCount = message.getRetryCount();
            
            log.info("处理购物车创建消息，用户ID: {}，重试次数: {}", userId, retryCount);
            
            // 执行创建购物车逻辑
            boolean success = createCartForUser(userId);
            
            if (!success && retryCount < 3) {
                // 失败且重试次数未达上限，重新入队
                message.setRetryCount(retryCount + 1);
                message.setTimestamp(System.currentTimeMillis());
                
                // 延迟重试 - 使用死信队列或延迟插件
                sendDelayedRetry(message, calculateDelay(retryCount));
                
                log.warn("购物车创建失败，准备第{}次重试，用户ID: {}", 
                        retryCount + 1, userId);
            } else if (!success) {
                // 达到最大重试次数
                log.error("购物车创建最终失败，用户ID: {}", userId);
                // 可以发送告警或记录到失败队列
                handleFinalFailure(userId);
            } else {
                log.info("购物车创建成功，用户ID: {}", userId);
            }
            
        } catch (Exception e) {
            log.error("处理购物车创建消息时发生异常", e);
        }
    }
    
    /**
     * 发送延迟重试消息
     */
    private void sendDelayedRetry(CartCreateMessage message, long delayMillis) {
        // 这里需要配置 RabbitMQ 延迟插件或使用死信队列
        // 简化处理，直接重新发送
        rabbitTemplate.convertAndSend(
            CART_CREATE_EXCHANGE,
            CART_CREATE_ROUTING_KEY,
            message
        );
    }
    
    /**
     * 计算延迟时间（指数退避）
     */
    private long calculateDelay(int retryCount) {
        return (long) Math.pow(2, retryCount) * 10000; // 10秒, 20秒, 40秒...
    }
    
    /**
     * 实际创建购物车的方法 - 调用真实的Feign服务
     */
    private boolean createCartForUser(Long userId) {
        try {
            return cartServiceClient.createCartForUser(userId);
            
        } catch (Exception e) {
            log.error("调用购物车服务异常，用户ID: {}", userId, e);
            return false;
        }
    }
    
    /**
     * 处理最终失败的情况
     */
    private void handleFinalFailure(Long userId) {
        // 记录到失败队列
        // 发送告警通知
        // 记录监控指标
        log.error("用户{}购物车创建彻底失败，需要人工干预", userId);
    }
    
    /**
     * 购物车创建消息实体
     */
    @Data
    public static class CartCreateMessage {
        private Long userId;
        private int retryCount;
        private long timestamp;
    }
}