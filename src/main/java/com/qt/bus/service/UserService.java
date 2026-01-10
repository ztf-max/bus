package com.qt.bus.service;

import com.qt.bus.dto.WxLoginRequest;
import com.qt.bus.dto.WxLoginResponse;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 微信小程序登录
     *
     * @param request 登录请求
     * @return 登录响应（包含token）
     */
    WxLoginResponse wxLogin(WxLoginRequest request);
}
