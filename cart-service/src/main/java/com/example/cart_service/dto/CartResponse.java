package com.example.cart_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 购物车响应DTO（简化版）
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@Data
public class CartResponse {
    private List<CartItemDto> items;
    private Integer totalItems;
    private BigDecimal totalPrice;
    private Integer selectedCount;

    @Data
    public static class CartItemDto {
        private Long productId;
        private Integer quantity;
        private Integer selected;       // 0:未选中, 1:选中
    }
}