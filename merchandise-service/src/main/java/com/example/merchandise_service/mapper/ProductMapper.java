package com.example.merchandise_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.merchandise_service.DO.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 商品Mapper接口
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    /**
     * 根据分类ID获取商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @Select("SELECT COUNT(*) FROM product WHERE category_id = #{categoryId} AND status = 1")
    Integer countByCategoryId(@Param("categoryId") Long categoryId);
}