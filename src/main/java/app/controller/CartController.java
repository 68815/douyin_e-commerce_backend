package app.controller;

import app.entity.Cart;
import app.service.ICartService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/cart")
@SaCheckLogin
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 获取用户购物车
     */
    @GetMapping
    public List<Cart> getCart() {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        return cartService.getUserCart(userId);
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public Map<String, Object> addToCart(@RequestParam Long productId, 
                                        @RequestParam Integer quantity) {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        boolean success = cartService.addToCart(userId, productId, quantity);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{cartId}")
    public Map<String, Object> updateQuantity(@PathVariable Long cartId, 
                                             @RequestParam Integer quantity) {
        boolean success = cartService.updateCartItemQuantity(cartId, quantity);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    /**
     * 从购物车移除商品
     */
    @DeleteMapping("/{cartId}")
    public Map<String, Object> removeFromCart(@PathVariable Long cartId) {
        boolean success = cartService.removeFromCart(cartId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "移除成功" : "移除失败");
        return result;
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public Map<String, Object> clearCart() {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        boolean success = cartService.clearCart(userId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", success);
        result.put("message", success ? "清空成功" : "清空失败");
        return result;
    }

    /**
     * 获取购物车商品数量
     */
    @GetMapping("/count")
    public Map<String, Object> getCartCount() {
        Long userId = cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong();
        int count = cartService.getCartItemCount(userId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("count", count);
        return result;
    }
}