package com.example.commonmodule.client;

import com.example.commonmodule.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "merchandise-service", path = "/products")
public interface ProductFeignClient {

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);

    @GetMapping("/getBatch")
    List<ProductResponse> getProductsByIds(@RequestParam("ids") List<Long> ids);
}
