package com.qt.bus.dto;

import lombok.Data;

/**
 * 微信登录请求DTO
 */
@Data
public class WxLoginRequest {

    /**
     * 微信登录凭证code
     */
    private String code;

    /**
     * 用户昵称（可选）
     */
    private String nickname;

    /**
     * 用户头像URL（可选）
     */
    private String avatarUrl;

    /**
     * 平台类型：USER-乘客端, DRIVER-司机端
     */
    private String platform;
}
