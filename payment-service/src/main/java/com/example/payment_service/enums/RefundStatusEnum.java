package com.example.payment_service.enums;

import lombok.Getter;

/**
 * 退款状态枚举
 */
@Getter
public enum RefundStatusEnum {
    
    PROCESSING(0, "处理中"),
    SUCCESS(1, "退款成功"),
    FAILED(2, "退款失败");
    
    private final Integer code;
    private final String description;
    
    RefundStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public static RefundStatusEnum fromCode(Integer code) {
        for (RefundStatusEnum status : RefundStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}