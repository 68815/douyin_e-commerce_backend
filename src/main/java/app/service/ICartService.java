package app.service;

import app.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface ICartService extends IService<Cart> {

    /**
     * 获取用户购物车
     */
    List<Cart> getUserCart(Long userId);

    /**
     * 添加商品到购物车
     */
    boolean addToCart(Long userId, Long productId, Integer quantity);

    /**
     * 更新购物车商品数量
     */
    boolean updateCartItemQuantity(Long cartId, Integer quantity);

    /**
     * 从购物车移除商品
     */
    boolean removeFromCart(Long cartId);

    /**
     * 清空用户购物车
     */
    boolean clearCart(Long userId);

    /**
     * 获取购物车商品总数
     */
    int getCartItemCount(Long userId);
}