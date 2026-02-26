package com.example.merchandise_service.repository;

import com.example.merchandise_service.entity.es.ProductEsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品ES Repository接口
 * 提供Elasticsearch数据访问能力
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-26
 */
@Repository
public interface ProductEsRepository extends ElasticsearchRepository<ProductEsDocument, Long> {
    
    /**
     * 根据商品名称搜索（支持模糊匹配）
     * @param productName 商品名称
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByProductNameContaining(String productName, Pageable pageable);
    
    /**
     * 根据商品描述搜索
     * @param productDescription 商品描述
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByProductDescriptionContaining(String productDescription, Pageable pageable);
    
    /**
     * 综合搜索（名称+描述）
     * @param keyword 搜索关键词
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByProductNameContainingOrProductDescriptionContaining(
            String keyword, String keyword2, Pageable pageable);
    
    /**
     * 根据分类名称搜索
     * @param categoryName 分类名称
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByCategoryName(String categoryName, Pageable pageable);
    
    /**
     * 根据价格范围搜索
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    
    /**
     * 获取热销商品
     * @param isHot 是否热销
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByHot(Boolean isHot, Pageable pageable);
    
    /**
     * 获取新品商品
     * @param isNew 是否新品
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByNew(Boolean isNew, Pageable pageable);
    
    /**
     * 根据状态获取商品
     * @param status 商品状态
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 复合条件搜索
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param status 商品状态
     * @param pageable 分页参数
     * @return 商品列表
     */
    Page<ProductEsDocument> findByProductNameContainingOrProductDescriptionContainingAndCategoryNameAndPriceBetweenAndStatus(
            String keyword1, String keyword2, String categoryName, 
            Double minPrice, Double maxPrice, Integer status, Pageable pageable);
}