package com.qt.bus.controller;

import com.qt.bus.dto.WxLoginRequest;
import com.qt.bus.dto.WxLoginResponse;
import com.qt.bus.model.response.Result;
import com.qt.bus.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 微信小程序一键登录
     * 
     * 请求示例：
     * {
     *   "code": "微信登录凭证",
     *   "nickname": "用户昵称（可选）",
     *   "avatarUrl": "头像URL（可选）",
     *   "platform": "USER或DRIVER"
     * }
     * 
     * @param request 登录请求
     * @return 登录结果（包含token）
     */
    @PostMapping("/wx-login")
    public Result<WxLoginResponse> wxLogin(@RequestBody WxLoginRequest request) {
        WxLoginResponse response = userService.wxLogin(request);
        return Result.success(response);
    }
}
