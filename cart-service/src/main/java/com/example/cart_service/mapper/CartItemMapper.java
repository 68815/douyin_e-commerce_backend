package com.example.cart_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cart_service.DO.CartItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

}