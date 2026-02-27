package com.example.cart_service.controller;

import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.entity.CartItem;
import com.example.cart_service.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 购物车控制器
 * </p>
 *
 * @author 0109
 * @since 2026-02-27
 */
@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    

    private final ICartService cartService;
    @Autowired
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addCartItem")
    public Long addCartItem(@RequestParam Long userId,
                        @RequestParam Long productId,
                        @RequestParam Integer quantity) {
        if(userId < 0 || productId < 0 || quantity == null || quantity <= 0) {
            return -1L;
        }
        return cartService.addCartItem(userId, productId, quantity);
    }

    /**
     * 获取用户购物车
     * @param userId 用户ID
     * @return 购物车信息
     */
    @GetMapping("/{userId}")
    public CartResponse getCartByUserId(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getUserCart(userId);
        
        CartResponse response = new CartResponse();
        response.setUserId(userId);
        response.setItems(cartItems);
        response.setTotalItems(cartItems.size());
        
        // 计算选中商品数量和总价
        int selectedCount = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        
        for (CartItem item : cartItems) {
            if (item.getSelected() == 1) {
                selectedCount += item.getQuantity();
                // 这里需要从商品服务获取实际价格
                totalPrice = totalPrice.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            }
        }
        
        response.setSelectedCount(selectedCount);
        response.setTotalPrice(totalPrice);
        
        return response;
    }
}