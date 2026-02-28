package com.example.commonmodule.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 商品服务降级处理
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@Slf4j
@Component
public class ProductServiceFallback implements ProductServiceClient {

    @Override
    public List<Integer> getProductsByIds(@RequestParam List<Long> ids) {
        return Collections.emptyList();
    }
}