package com.example.merchandise_service.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.merchandise_service.dto.PageResponse;
import com.example.merchandise_service.dto.ProductResponse;
import com.example.merchandise_service.dto.ProductSearchRequest;
import com.example.merchandise_service.entity.Product;
import com.example.merchandise_service.mapper.ProductMapper;
import com.example.merchandise_service.service.IProductService;
import com.example.merchandise_service.service.ProductSyncService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品服务实现类
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    private final ProductMapper productMapper;

    private final ProductSyncService productSyncService;
    
    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductSyncService productSyncService) {
        this.productMapper = productMapper;
        this.productSyncService = productSyncService;
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(Integer page, Integer size) {
        Page<Product> productPage = new Page<>(page, size);
        IPage<Product> pageResult = productMapper.selectPage(productPage, null);
        
        List<ProductResponse> productResponses = pageResult.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
                
        return PageResponse.of(productResponses, pageResult.getTotal(), page, size);
    }

    @Override
    public List<ProductResponse> getSpecifyQuantity(Integer quantity) {
        // 获取指定数量的商品，按创建时间倒序排列
        Page<Product> productPage = new Page<>(1, quantity);
        IPage<Product> pageResult = productMapper.selectPage(productPage, 
                lambdaQuery()
                        .eq(Product::getStatus, 1)
                        .orderByDesc(Product::getCreateTime));
        
        return pageResult.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ProductResponse> getProductsBySearch(ProductSearchRequest request) {
        Page<Product> productPage = new Page<>(request.getPage(), request.getSize());
        
        // 构建查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        // 关键词搜索 - 性能优化版本
        String keyword = request.getKeyword();
        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            // 使用全文索引优化，优先匹配商品名称
            String finalKeyword = keyword;
            queryWrapper.and(wrapper -> wrapper
                    .like("product_name", finalKeyword)
                    .or()
                    .like("product_description", finalKeyword));
        }

        // 分类筛选
        if (request.getCategoryId() != null) {
            queryWrapper.eq("category_id", request.getCategoryId());
        }

        // 状态筛选
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        }

        // 热销筛选
        if (request.getIsHot() != null) {
            queryWrapper.eq("is_hot", request.getIsHot());
        }

        // 新品筛选
        if (request.getIsNew() != null) {
            queryWrapper.eq("is_new", request.getIsNew());
        }

        // 排序
        if (request.getSortBy() != null) {
            switch (request.getSortBy()) {
                case "price_asc":
                    queryWrapper.orderByAsc("price");
                    break;
                case "price_desc":
                    queryWrapper.orderByDesc("price");
                    break;
                case "sales_desc":
                    queryWrapper.orderByDesc("sales_count", "created_time");
                    break;
                case "created_desc":
                default:
                    queryWrapper.orderByDesc("create_time");
                    break;
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        
        IPage<Product> pageResult = productMapper.selectPage(productPage, queryWrapper);
        
        List<ProductResponse> productResponses = pageResult.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
                
        return PageResponse.of(productResponses, pageResult.getTotal(), request.getPage(), request.getSize());
    }

    @Override
    public PageResponse<ProductResponse> getProductsByFilter(ProductSearchRequest request) {
        // 筛选功能与搜索功能共用同一个方法，可以根据需要分离
        return getProductsBySearch(request);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product != null && product.getStatus() == 1) {
            return convertToResponse(product);
        }
        return null;
    }

    @Override
    public List<ProductResponse> getHotProducts(Integer limit) {
        Page<Product> productPage = new Page<>(1, limit);
        IPage<Product> pageResult = productMapper.selectPage(productPage,
                lambdaQuery()
                        .eq(Product::getStatus, 1)
                        .eq(Product::getIsHot, 1)
                        .orderByDesc(Product::getSalesCount)
                        .orderByDesc(Product::getCreateTime));
        
        return pageResult.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getNewProducts(Integer limit) {
        Page<Product> productPage = new Page<>(1, limit);
        IPage<Product> pageResult = productMapper.selectPage(productPage,
                lambdaQuery()
                        .eq(Product::getStatus, 1)
                        .eq(Product::getIsNew, 1)
                        .orderByDesc(Product::getCreateTime));
        
        return pageResult.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将Product实体转换为ProductResponse DTO
     */
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        BeanUtils.copyProperties(product, response);
        return response;
    }
}