package com.qt.bus.dto;

import lombok.Data;

/**
 * 账户密码登录请求DTO
 */
@Data
public class PasswordLoginRequest {

    /**
     * 手机号或用户名
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 平台类型：USER-乘客端, DRIVER-司机端（可选，默认为USER）
     */
    private String platform;
}
