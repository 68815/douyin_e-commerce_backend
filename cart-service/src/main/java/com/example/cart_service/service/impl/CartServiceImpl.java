package com.example.cart_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.commonmodule.dto.CartItemDetailDto;
import com.example.cart_service.DO.CartItem;
import com.example.cart_service.mapper.CartItemMapper;
import com.example.cart_service.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车服务实现类
 */
@Slf4j
@Service
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements ICartService {
    
    private final CartItemMapper cartItemMapper;
    private final RedissonClient redissonClient;
    
    @Autowired
    public CartServiceImpl(CartItemMapper cartItemMapper, RedissonClient redissonClient) {
        this.cartItemMapper = cartItemMapper;
        this.redissonClient = redissonClient;
    }

    @Override
    public List<CartItemDetailDto> getUserCart(Long userId) {
        try {
            // 先从Redisson缓存获取
            RMap<String, List<CartItemDetailDto>> cartCache = redissonClient.getMap("cart_cache");
            List<CartItemDetailDto> cachedCart = cartCache.get("user_" + userId);
            
            if (cachedCart != null && !cachedCart.isEmpty()) {
                return cachedCart;
            }
            
            // 缓存未命中，从数据库查询
            QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .orderByDesc("created_time");
            List<CartItem> result = cartItemMapper.selectList(wrapper);
            List<CartItemDetailDto> cartDtoList = new ArrayList<CartItemDetailDto>();
            // 写入缓存
            if (!result.isEmpty()) {
                //调用merchandise服务，批量获取每个商品的name、imageurl、price
                List<Long> productIds = result.stream().map(CartItem::getProductId).toList();
                
                for (CartItem cartItem : result) {
                    cartDtoList.add(new CartItemDetailDto(
                            cartItem.getProductId(),
                            "sdasd",
                            "45",
                            cartItem.getQuantity(),
                            new BigDecimal("10.00"),
                            cartItem.getSelected()));
                }
                cartCache.put("user_" + userId, cartDtoList);
            }
            return cartDtoList;
        } catch (Exception e) {
            log.error("查询用户购物车失败，用户ID: {}", userId, e);
            return List.of();
        }
    }

    @Override
    @Transactional
    public boolean updateCartItemQuantity(Long userId, Integer quantity, Long productId) {
        try {
            UpdateWrapper<CartItem> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id", userId)
                   .eq("product_id", productId)
                   .set("quantity", quantity);
            int result = cartItemMapper.update(null, wrapper);
            if (result > 0) {
                clearUserCartCache(userId);
            }
            return result > 0;
        } catch (Exception e) {
            log.error("更新购物车商品数量失败，用户ID: {}, 商品ID: {}, 数量: {}", 
                    userId, productId, quantity, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeFromCart(Long userId, Long productId) {
        try {
            QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .eq("product_id", productId);
            
            int result = cartItemMapper.delete(wrapper);
            if (result > 0) {
                clearUserCartCache(userId);
            }
            return result > 0;
        } catch (Exception e) {
            log.error("删除购物车商品失败，用户ID: {}, 商品ID: {}", userId, productId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean clearCart(Long userId) {
        try {
            QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            int result = cartItemMapper.delete(wrapper);
            if (result > 0) {
                clearUserCartCache(userId);
            }
            return result > 0;
        } catch (Exception e) {
            log.error("清空用户购物车失败，用户ID: {}", userId, e);
            return false;
        }
    }

    @Override
    public int getCartItemCount(Long userId) {
        try {
            QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            long count = cartItemMapper.selectCount(wrapper);
            return Math.toIntExact(count);
        } catch (Exception e) {
            log.error("统计购物车商品数量失败，用户ID: {}", userId, e);
            return 0;
        }
    }

    @Override
    @Transactional
    public Long addCartItem(Long userId, Long productId, Integer quantity) {
        try {
            QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("product_id", productId);
            
            CartItem existingItem = cartItemMapper.selectOne(queryWrapper);
            
            Long itemId;
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setUpdatedTime(LocalDateTime.now());
                cartItemMapper.updateById(existingItem);
                itemId = existingItem.getItemId();
            } else {
                CartItem newItem = new CartItem();
                newItem.setUserId(userId);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                newItem.setSelected(1);
                newItem.setCreatedTime(LocalDateTime.now());
                newItem.setUpdatedTime(LocalDateTime.now());
                
                cartItemMapper.insert(newItem);
                itemId = newItem.getItemId();
            }
            clearUserCartCache(userId);
            return itemId;
        } catch (Exception e) {
            log.error("添加商品到购物车失败，用户ID: {}, 商品ID: {}, 数量: {}", 
                    userId, productId, quantity, e);
            return 0L;
        }
    }
    
    /**
     * 清除用户购物车缓存
     */
    private void clearUserCartCache(Long userId) {
        try {
            RMap<String, List<CartItem>> cartCache = redissonClient.getMap("cart_cache");
            cartCache.remove("user_" + userId);
        } catch (Exception e) {
            log.warn("清除用户购物车缓存失败，用户ID: {}", userId, e);
        }
    }
}