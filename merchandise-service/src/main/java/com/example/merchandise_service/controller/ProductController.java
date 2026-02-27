package com.example.merchandise_service.controller;

import com.example.merchandise_service.dto.PageResponse;
import com.example.merchandise_service.dto.ProductResponse;
import com.example.merchandise_service.dto.ProductSearchRequest;
import com.example.merchandise_service.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品控制器
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {


    private final IProductService productService;
    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * 获取所有商品（分页）
     * 对应前端接口: /products/getAll
     */
    @GetMapping("/getAll")
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        PageResponse<ProductResponse> result = productService.getAllProducts(page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取指定数量的商品
     * 对应前端接口: /products/getSpecifyQuantity
     */
    @GetMapping("/getSpecifyQuantity")
    public ResponseEntity<List<ProductResponse>> getSpecifyQuantity(
            @RequestParam(defaultValue = "100") Integer quantity) {
        List<ProductResponse> result = productService.getSpecifyQuantity(quantity);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据搜索条件获取商品
     * RESTful标准: GET /products/search
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer isHot,
            @RequestParam(required = false) Integer isNew,
            @RequestParam(defaultValue = "created_desc") String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        ProductSearchRequest request = new ProductSearchRequest();
        request.setKeyword(keyword);
        request.setCategoryId(categoryId);
        request.setStatus(status);
        request.setIsHot(isHot);
        request.setIsNew(isNew);
        request.setSortBy(sortBy);
        request.setPage(page);
        request.setSize(size);

        PageResponse<ProductResponse> result = productService.getProductsBySearch(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据筛选条件获取商品
     * 对应前端接口: /products/getByFilter
     */
    @PostMapping("/getByFilter")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByFilter(
            @RequestBody ProductSearchRequest request) {

        PageResponse<ProductResponse> result = productService.getProductsByFilter(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据ID获取商品详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        
        ProductResponse result = productService.getProductById(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取热销商品
     */
    @GetMapping("/hot")
    public ResponseEntity<List<ProductResponse>> getHotProducts(
            @RequestParam(defaultValue = "10") Integer limit) {

        List<ProductResponse> result = productService.getHotProducts(limit);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取新品商品
     */
    @GetMapping("/new")
    public ResponseEntity<List<ProductResponse>> getNewProducts(
            @RequestParam(defaultValue = "10") Integer limit) {

        List<ProductResponse> result = productService.getNewProducts(limit);
        return ResponseEntity.ok(result);
    }
}