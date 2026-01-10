package com.qt.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信登录响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxLoginResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * JWT Token
     */
    private String token;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 是否新用户
     */
    private Boolean isNewUser;
}
