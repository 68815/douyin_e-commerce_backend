package com.example.user_service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 验证码实体类
 * </p>
 *
 * @author 0109
 * @since 2025-07-19
 */
@Data
@TableName("verification_code")
public class VerificationCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "verification_code_id", type = IdType.AUTO)
    private Long verificationCodeId;

    @TableField("email_or_phone")
    private String emailOrPhone;

    @TableField("verify_code")
    private String verifyCode;

    @TableField("is_used")
    private int isUsed = 0;

    @TableField("created_at")
    private LocalDateTime createdAt;
}