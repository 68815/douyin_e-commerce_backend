package com.example.commonmodule.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品响应DTO
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Data
public class ProductResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private Integer stockQuantity;
    private Long categoryId;

    private String imageUrl;
    private Integer status;
    private Integer salesCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isHot;
    private Integer isNew;
}