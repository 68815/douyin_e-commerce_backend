package com.example.merchandise_service.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品实体类
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Data
@TableName("product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品描述
     */
    @TableField("product_description")
    private String productDescription;

    /**
     * 商品价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 商品库存
     */
    @TableField("stock_quantity")
    private Integer stockQuantity;

    /**
     * 商品分类ID
     */
    @TableField("category_id")
    private Long categoryId;



    /**
     * 商品图片URL
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 商品状态 (0:下架, 1:上架)
     */
    @TableField("status")
    private Integer status;

    /**
     * 销售数量
     */
    @TableField("sales_count")
    private Integer salesCount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否热销 (0:否, 1:是)
     */
    @TableField("is_hot")
    private Integer isHot;

    /**
     * 是否新品 (0:否, 1:是)
     */
    @TableField("is_new")
    private Integer isNew;
}