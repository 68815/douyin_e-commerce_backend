package com.example.cart_service.controller;

import com.example.cart_service.dto.CartItemDetailDto;
import com.example.cart_service.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (userId < 0 || productId < 0 || quantity == null || quantity <= 0) {
            return -1L;
        }
        return cartService.addCartItem(userId, productId, quantity);
    }

    /**
     * 获取用户购物车
     *
     * @param userId 用户ID
     * @return 购物车信息
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDetailDto>> getCartByUserId(@PathVariable Long userId) {
        if(userId < 0) return ResponseEntity.badRequest().build();
        List<CartItemDetailDto> cart = cartService.getUserCart(userId);
        if (cart == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cart);
    }

    /**
     * 删除购物车中的商品
     *
     * @param userId    用户ID
     * @param productId 商品ID
     * @return 是否删除成功
     */
    @DeleteMapping("/remove/{userId}/{productId}")
    public boolean deleteCartItem(@PathVariable Long userId,
                                  @PathVariable Long productId) {
        return cartService.removeFromCart(userId, productId);
    }

    @PutMapping("/updateCartItem/{userId}/{productId}/{quantity}")
    public boolean updateCartItem(@PathVariable Long userId,
                                  @PathVariable Long productId,
                                  @PathVariable Integer quantity) {
        if (userId < 0 || productId < 0 || quantity == null || quantity <= 0) {
            return false;
        }
        return cartService.updateCartItemQuantity(userId, quantity, productId);
    }
}