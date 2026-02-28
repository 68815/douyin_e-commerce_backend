package com.example.commonmodule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 商品服务Feign客户端
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@FeignClient(name = "merchandise-service", fallback = ProductServiceFallback.class)
public interface ProductServiceClient {
    
    /**
     * 批量获取商品信息
     * @param ids 商品ID列表
     * @return 商品信息
     */
    @GetMapping("/getBatch")
    public List<Integer> getProductsByIds(@RequestParam List<Long> ids);

}