package com.example.order_service.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String idempotentKey;

    private Long userId;

    private String shippingAddress;

    private String paymentMethod;

    private List<CreateOrderItemRequest> items;

    @Data
    public static class CreateOrderItemRequest implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Long productId;

        private String productName;

        private String productImage;

        private String skuInfo;

        private Integer quantity;

        private BigDecimal price;
    }
}
