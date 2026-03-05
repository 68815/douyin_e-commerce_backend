package com.example.cart_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cart_service.DO.CartItem;
import com.example.cart_service.mapper.CartItemMapper;
import com.example.cart_service.service.ICartService;
import com.example.commonmodule.dto.CartItemDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    
    @Autowired
    public CartServiceImpl(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    @Cacheable(value = "cart", key = "'user:' + #userId")
    public List<CartItemDetailDto> getUserCart(Long userId) {
        // 从数据库查询
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .orderByDesc("created_time");
        List<CartItem> result = cartItemMapper.selectList(wrapper);
        List<CartItemDetailDto> cartDtoList = new ArrayList<CartItemDetailDto>();
            
        if (!result.isEmpty()) {
            for (CartItem cartItem : result) {
                cartDtoList.add(new CartItemDetailDto(
                        cartItem.getProductId(),
                        "sdasd",
                        "45",
                        cartItem.getQuantity(),
                        new BigDecimal("10.00"),
                        cartItem.getSelected()));
            }
        }
        return cartDtoList;
    }

    @Override
    @Transactional
    @CacheEvict(value = "cart", key = "'user:' + #userId")
    public boolean updateCartItemQuantity(Long userId, Integer quantity, Long productId) {
        try {
            UpdateWrapper<CartItem> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id", userId)
                   .eq("product_id", productId)
                   .set("quantity", quantity);
            int result = cartItemMapper.update(null, wrapper);
            return result > 0;
        } catch (Exception e) {
            log.error("更新购物车商品数量失败，用户ID: {}, 商品ID: {}, 数量: {}", 
                    userId, productId, quantity, e);
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "cart", key = "'user:' + #userId")
    public boolean removeFromCart(Long userId, Long productId) {
        try {
            QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId)
                   .eq("product_id", productId);
            
            int result = cartItemMapper.delete(wrapper);
            return result > 0;
        } catch (Exception e) {
            log.error("删除购物车商品失败，用户ID: {}, 商品ID: {}", userId, productId, e);
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "cart", key = "'user:' + #userId")
    public boolean clearCart(Long userId) {
        try {
            QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            int result = cartItemMapper.delete(wrapper);
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
    @CacheEvict(value = "cart", key = "'user:' + #userId")
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
            return itemId;
        } catch (Exception e) {
            log.error("添加商品到购物车失败，用户ID: {}, 商品ID: {}, 数量: {}", 
                    userId, productId, quantity, e);
            return 0L;
        }
    }
}