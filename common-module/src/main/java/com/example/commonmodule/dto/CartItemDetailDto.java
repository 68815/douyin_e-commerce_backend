package com.example.commonmodule.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 购物车响应DTO（简化版）
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@Data
public class CartItemDetailDto {
    private String imageUrl;
    private String productName;

    private Long productId;
    private Integer quantity;
    private Integer selected;       // 0:未选中, 1:选中
    private BigDecimal price;

        public CartItemDetailDto(Long productId, String productName, String imageUrl,
                                 Integer quantity,BigDecimal price, Integer selected) {
            this.productId = productId;
            this.productName = productName;
            this.imageUrl = imageUrl;
            this.quantity = quantity;
            this.selected = selected;
            this.price = price;
        }
}