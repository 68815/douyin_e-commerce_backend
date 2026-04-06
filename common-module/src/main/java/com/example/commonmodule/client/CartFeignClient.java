package com.example.commonmodule.client;

import com.example.commonmodule.dto.CartItemDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "cart-service", path = "/cart")
public interface CartFeignClient {

    @GetMapping("/{userId}")
    List<CartItemDetailDto> getCartByUserId(@PathVariable("userId") Long userId);
}
