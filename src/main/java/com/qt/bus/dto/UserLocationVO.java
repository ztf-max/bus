package com.qt.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 乘客位置信息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationVO {

    /**
     * 乘客ID
     */
    private Long userId;

    /**
     * 乘客昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;
}
