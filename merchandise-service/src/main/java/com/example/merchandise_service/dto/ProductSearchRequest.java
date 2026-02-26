package com.example.merchandise_service.dto;

import lombok.Data;

/**
 * <p>
 * 商品搜索请求DTO
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Data
public class ProductSearchRequest {
    
    private String keyword;
    private Long categoryId;
    private Integer status;
    private Integer isHot;
    private Integer isNew;
    private String sortBy; // price_asc, price_desc, sales_desc, created_desc
    private Integer page = 1;
    private Integer size = 20;
}