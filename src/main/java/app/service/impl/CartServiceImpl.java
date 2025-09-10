package app.service.impl;

import app.entity.Cart;
import app.entity.Product;
import app.mapper.CartMapper;
import app.service.ICartService;
import app.service.IProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Autowired
    private IProductService productService;

    @Override
    public List<Cart> getUserCart(Long userId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        return list(queryWrapper);
    }

    @Override
    public boolean addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否存在
        Product product = productService.getById(productId);
        if (product == null) {
            return false;
        }

        // 检查购物车是否已有该商品
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId)
                   .eq(Cart::getProductId, productId);
        Cart existingCart = getOne(queryWrapper);

        if (existingCart != null) {
            // 更新数量
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            existingCart.setUpdatedAt(LocalDateTime.now());
            return updateById(existingCart);
        } else {
            // 新增商品到购物车
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            return save(cart);
        }
    }

    @Override
    public boolean updateCartItemQuantity(Long cartId, Integer quantity) {
        Cart cart = getById(cartId);
        if (cart == null) {
            return false;
        }
        cart.setQuantity(quantity);
        cart.setUpdatedAt(LocalDateTime.now());
        return updateById(cart);
    }

    @Override
    public boolean removeFromCart(Long cartId) {
        return removeById(cartId);
    }

    @Override
    public boolean clearCart(Long userId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        return remove(queryWrapper);
    }

    @Override
    public int getCartItemCount(Long userId) {
        LambdaQueryWrapper<Cart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Cart::getUserId, userId);
        return (int) count(queryWrapper);
    }
}