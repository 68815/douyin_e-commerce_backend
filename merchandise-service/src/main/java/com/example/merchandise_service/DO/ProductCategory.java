package com.example.merchandise_service.DO;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品分类实体类
 * </p>
 *
 * @author ecommerce
 * @since 2026-02-21
 */
@Data
@TableName("product_category")
public class ProductCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 父分类ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 分类层级
     */
    @TableField("level")
    private Integer level;

    /**
     * 分类排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 分类图标
     */
    @TableField("icon_url")
    private String iconUrl;

    /**
     * 分类状态 (0:禁用, 1:启用)
     */
    @TableField("status")
    private Integer status;

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
}