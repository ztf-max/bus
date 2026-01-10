package com.qt.bus.service.impl;

import com.qt.bus.dao.model.User;
import com.qt.bus.dao.repository.UserRepository;
import com.qt.bus.dto.WxLoginRequest;
import com.qt.bus.dto.WxLoginResponse;
import com.qt.bus.dto.WxSessionResponse;
import com.qt.bus.exception.SystemException;
import com.qt.bus.interceptor.JwtUtils;
import com.qt.bus.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RestClient restClient;

    /**
     * 微信小程序AppID
     */
    @Value("${wechat.appid:}")
    private String appId;

    /**
     * 微信小程序AppSecret
     */
    @Value("${wechat.secret:}")
    private String appSecret;

    /**
     * 用户类型：司机
     */
    private static final String USER_TYPE_DRIVER = "driver";

    /**
     * 用户类型：乘客
     */
    private static final String USER_TYPE_USER = "user";

    /**
     * 微信code2Session接口URL
     */
    private static final String WX_CODE2SESSION_URL = 
        "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxLoginResponse wxLogin(WxLoginRequest request) {
        // 1. 校验参数
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new SystemException("登录凭证code不能为空", 400);
        }

        String platform = request.getPlatform();
        if (platform == null || platform.isEmpty()) {
            platform = USER_TYPE_USER; // 默认为乘客
        }

        // 2. 调用微信接口获取openid
        String openid = getOpenidFromWx(request.getCode());

        // 3. 查询或创建用户
        User user = userRepository.getByOpenid(openid);
        boolean isNewUser = false;
        String userType;

        if (user == null) {
            // 新用户，创建记录
            user = new User();
            user.setOpenid(openid);
            
            // 根据请求参数确定用户类型
            if (USER_TYPE_DRIVER.equalsIgnoreCase(platform)) {
                userType = USER_TYPE_DRIVER;
                user.setUserType(USER_TYPE_DRIVER);
                user.setNickname(request.getNickname() != null ? request.getNickname() : "司机用户");
                user.setWorkStatus(0); // 默认收车状态
            } else {
                userType = USER_TYPE_USER;
                user.setUserType(USER_TYPE_USER);
                user.setNickname(request.getNickname() != null ? request.getNickname() : "微信用户");
            }
            
            user.setAvatarUrl(request.getAvatarUrl());
            user.setCreator(openid);
            user.setModifier(openid);
            userRepository.save(user);
            isNewUser = true;
            log.info("新用户注册 - OpenID: {}, UserID: {}, UserType: {}", openid, user.getId(), userType);
        } else {
            // 已存在用户，更新信息
            userType = user.getUserType();
            
            // 更新昵称和头像（如果提供）
            boolean needUpdate = false;
            if (request.getNickname() != null && !request.getNickname().isEmpty()) {
                user.setNickname(request.getNickname());
                needUpdate = true;
            }
            if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
                user.setAvatarUrl(request.getAvatarUrl());
                needUpdate = true;
            }
            
            if (needUpdate) {
                user.setModifier(openid);
                userRepository.updateById(user);
            }
        }

        // 4. 生成JWT Token（30天有效期，Token会自动保存到数据库和上下文）
        String token = jwtUtils.createToken(user.getNickname(), userType, user.getId(), userType, user.getId());

        // 5. 返回登录结果
        return WxLoginResponse.builder()
                .userId(user.getId())
                .token(token)
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .isNewUser(isNewUser)
                .build();
    }

    /**
     * 从微信服务器获取OpenID
     */
    private String getOpenidFromWx(String code) {
        try {
            String url = String.format(WX_CODE2SESSION_URL, appId, appSecret, code);
            
            // 使用 RestClient 替代 RestTemplate
            WxSessionResponse response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(WxSessionResponse.class);

            if (response == null) {
                throw new SystemException("微信登录失败：无响应", 500);
            }

            if (response.getErrcode() != null && response.getErrcode() != 0) {
                log.error("微信登录失败 - errcode: {}, errmsg: {}", response.getErrcode(), response.getErrmsg());
                throw new SystemException("微信登录失败：" + response.getErrmsg(), 500);
            }

            if (response.getOpenid() == null || response.getOpenid().isEmpty()) {
                throw new SystemException("微信登录失败：未获取到OpenID", 500);
            }

            return response.getOpenid();
        } catch (SystemException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用微信接口失败", e);
            throw new SystemException("微信登录失败，请稍后重试", 500);
        }
    }

}
