package com.example.merchandise_service.service;

import com.example.merchandise_service.entity.Product;
import com.example.merchandise_service.entity.es.ProductEsDocument;
import com.example.merchandise_service.repository.ProductEsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 商品数据同步服务
 * 负责 MySQL 和 Elasticsearch 之间的数据同步
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-26
 */
@Slf4j
@Service
public class ProductSyncService {
    
    @Autowired
    private ProductEsRepository productEsRepository;
    
    /**
     * 同步商品到 Elasticsearch
     * @param product MySQL中的商品实体
     */
    @Async
    public void syncProductToEs(Product product) {
        try {
            ProductEsDocument esDocument = convertToEsDocument(product);
            productEsRepository.save(esDocument);
            log.info("商品同步到ES成功，商品ID: {}", product.getProductId());
        } catch (Exception e) {
            log.error("商品同步到ES失败，商品ID: {}", product.getProductId(), e);
        }
    }
    
    /**
     * 从 Elasticsearch 删除商品
     * @param productId 商品ID
     */
    @Async
    public void deleteProductFromEs(Long productId) {
        try {
            productEsRepository.deleteById(productId);
            log.info("从ES删除商品成功，商品ID: {}", productId);
        } catch (Exception e) {
            log.error("从ES删除商品失败，商品ID: {}", productId, e);
        }
    }
    
    /**
     * 更新 ES 中的商品信息
     * @param product 更新后的商品实体
     */
    @Async
    public void updateProductInEs(Product product) {
        try {
            ProductEsDocument esDocument = convertToEsDocument(product);
            productEsRepository.save(esDocument);
            log.info("更新ES中商品信息成功，商品ID: {}", product.getProductId());
        } catch (Exception e) {
            log.error("更新ES中商品信息失败，商品ID: {}", product.getProductId(), e);
        }
    }
    
    /**
     * 将 MySQL 实体转换为 ES 文档
     * @param product MySQL商品实体
     * @return ES文档实体
     */
    private ProductEsDocument convertToEsDocument(Product product) {
        ProductEsDocument esDocument = new ProductEsDocument();
        
        // 基本字段映射
        esDocument.setProductId(product.getProductId());
        esDocument.setProductName(product.getProductName());
        esDocument.setProductDescription(product.getProductDescription());
        esDocument.setPrice(product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO);
        esDocument.setStockQuantity(product.getStockQuantity() != null ? product.getStockQuantity() : 0);
        esDocument.setSalesCount(product.getSalesCount() != null ? product.getSalesCount() : 0);
        esDocument.setHot(product.getIsHot() != null && product.getIsHot() == 1);
        esDocument.setNew(product.getIsNew() != null && product.getIsNew() == 1);
        esDocument.setStatus(product.getStatus());
        esDocument.setCreateTime(product.getCreateTime());
        esDocument.setUpdateTime(product.getUpdateTime());
        
        return esDocument;
    }
    
    /**
     * 批量同步商品到 ES
     * @param products 商品列表
     */
    @Async
    public void batchSyncProductsToEs(Iterable<Product> products) {
        try {
            productEsRepository.saveAll(
                () -> products.iterator()
                    .forEachRemaining(this::convertToEsDocument)
            );
            log.info("批量同步商品到ES完成");
        } catch (Exception e) {
            log.error("批量同步商品到ES失败", e);
        }
    }
}