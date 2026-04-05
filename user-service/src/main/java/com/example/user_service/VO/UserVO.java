package com.example.user_service.VO;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;

    private String userEmail;

    private String userPhoneNumber;

    private LocalDateTime registerDateTime;

    private LocalDateTime latestLoginDateTime;

    private Integer totalLoginDays;

    private Integer consecutiveLoginDays;

    private Integer trustScore;
}
