package com.example.user_service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Data
@TableName("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("user_password")
    private String userPassword;

    @TableField("user_email")
    private String userEmail;

    @TableField("user_phone_number")
    private String userPhoneNumber;

    @TableField("register_datetime")
    private LocalDateTime registerDateTime;

    @TableField("lastest_login_datetime")
    private LocalDateTime latestLoginDateTime;

    @TableField("total_login_days")
    private Integer totalLoginDays;

    @TableField("consecutive_login_days")
    private Integer consecutiveLoginDays;

    @TableField("trust_score")
    private Integer trustScore;
}