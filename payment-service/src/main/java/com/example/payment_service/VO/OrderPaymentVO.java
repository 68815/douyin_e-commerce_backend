package com.example.payment_service.VO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单支付VO（通用）
 * 适用于直接购买和购物车结算两种场景
 */
@Data
public class OrderPaymentVO {
    
    // 支付基本信息
    private String paymentNo;           // 支付流水号
    private String orderNo;             // 订单号
    private Long userId;                // 用户ID
    private BigDecimal totalAmount;     // 总金额
    
    // 支付状态
    private Integer paymentStatus;      // 支付状态 (0:待支付, 1:支付成功, 2:支付失败, 3:已退款)
    private String paymentMethod;       // 支付方式
    private String transactionId;       // 第三方交易ID
    
    // 时间信息
    private LocalDateTime createdTime;  // 创建时间
    private LocalDateTime paidTime;     // 支付完成时间
    private LocalDateTime expiredTime;  // 过期时间
    
    // 订单明细（支持单商品和多商品）
    private List<OrderItemInfo> orderItems;
    
    // 统计信息
    private Integer totalQuantity;      // 商品总数量
    
    // 支付链接
    private String payUrl;              // 支付链接
    
    @Data
    public static class OrderItemInfo {
        private Long productId;         // 商品ID
        private String productName;     // 商品名称
        private String imageUrl;        // 商品图片
        private Integer quantity;       // 购买数量
        private BigDecimal price;       // 单价
        private BigDecimal subtotal;    // 小计
        
        // 购物车特有字段（直接购买时可忽略）
        private Integer selected;       // 是否选中 (0:未选中, 1:选中)
    }
}