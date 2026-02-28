package com.example.cart_service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.commonmodule.dto.CartItemDetailDto;
import com.example.cart_service.DO.CartItem;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface ICartService extends IService<CartItem> {

    /**
     * 获取用户购物车
     */
    List<CartItemDetailDto> getUserCart(Long userId);

    /**
     * 更新购物车商品数量
     */
    boolean updateCartItemQuantity(Long userId, Integer quantity, Long productId);

    /**
     * 从购物车移除商品
     */
    boolean removeFromCart(Long userId, Long productId);

    /**
     * 清空用户购物车
     */
    boolean clearCart(Long userId);

    /**
     * 获取购物车商品总数
     */
    int getCartItemCount(Long userId);

    /**
     * 添加购物车项
     *
     * @param quantity 数量
     * @return 购物车项ID
     */
    Long addCartItem(Long userId, Long productId, Integer quantity);
}