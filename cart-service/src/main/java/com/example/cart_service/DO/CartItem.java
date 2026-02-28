package com.example.cart_service.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 购物车项实体类
 * </p>
 *
 * @author 0109
 * @since 2026-02-28
 */
@Data
@TableName("cart_item")
public class CartItem implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;
    
    @TableField("user_id")
    private Long userId;
    
    @TableField("product_id")
    private Long productId;
    
    @TableField("quantity")
    private Integer quantity;
    
    @TableField("selected")
    private Integer selected; // 0:未选中, 1:选中
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}