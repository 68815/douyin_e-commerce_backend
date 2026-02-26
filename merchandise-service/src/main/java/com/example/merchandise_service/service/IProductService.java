package com.example.merchandise_service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.merchandise_service.dto.PageResponse;
import com.example.merchandise_service.dto.ProductResponse;
import com.example.merchandise_service.dto.ProductSearchRequest;
import com.example.merchandise_service.entity.Product;

import java.util.List;

/**
 * <p>
 * 商品服务接口
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
public interface IProductService {
    
    /**
     * 获取所有商品（分页）
     * @param page 页码
     * @param size 每页数量
     * @return 商品列表
     */
    PageResponse<ProductResponse> getAllProducts(Integer page, Integer size);
    
    /**
     * 获取指定数量的商品
     * @param quantity 数量
     * @return 商品列表
     */
    List<ProductResponse> getSpecifyQuantity(Integer quantity);
    
    /**
     * 根据搜索条件获取商品
     * @param request 搜索请求
     * @return 分页商品列表
     */
    PageResponse<ProductResponse> getProductsBySearch(ProductSearchRequest request);
    
    /**
     * 根据筛选条件获取商品
     * @param request 筛选请求
     * @return 分页商品列表
     */
    PageResponse<ProductResponse> getProductsByFilter(ProductSearchRequest request);
    
    /**
     * 根据ID获取商品详情
     * @param productId 商品ID
     * @return 商品详情
     */
    ProductResponse getProductById(Long productId);
    
    /**
     * 获取热销商品
     * @param limit 数量限制
     * @return 热销商品列表
     */
    List<ProductResponse> getHotProducts(Integer limit);
    
    /**
     * 获取新品商品
     * @param limit 数量限制
     * @return 新品商品列表
     */
    List<ProductResponse> getNewProducts(Integer limit);
}