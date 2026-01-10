package com.qt.bus.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qt.bus.constants.CommonConstant;
import com.qt.bus.dao.model.Token;
import com.qt.bus.dao.repository.TokenRepository;
import com.qt.bus.exception.SystemException;
import com.qt.bus.jwt.AuthLoginContextHolder;
import com.qt.bus.jwt.JwtUserLoginInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * JWT工具类
 */
@Component
@Slf4j
public class JwtUtils {

    // 私钥
    private static final String TOKEN_SECRET = "##@$%@#S#WS";

    @Resource
    private TokenRepository tokenRepository;


    /**
     * 验证token
     */
    public void verify(String token) {

        try {
            JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build().verify(token);
        } catch (Exception e) {
            if (e instanceof TokenExpiredException) {
                HttpServletRequest request = getRequest();
                String ip = Objects.isNull(request) ? "" : request.getHeader("X-Forwarded-For");
                String uri = Objects.isNull(request) ? "" : request.getRequestURI();
                log.error("JWT已过期！ ip: {}, uri: {}, token: {}", ip, uri, token, e);
                throw new SystemException("登录后查看完整信息", 401);
            }
            // 效验失败
            log.error("JWT验证失败！", e);
            throw new SystemException("登录后查看完整信息", 401);
        }

        // 解析Token并保存到上下文
        JwtUserLoginInfo loginInfo = JwtUserLoginInfo.builder()
                .nickName(getClaimByName(token, CommonConstant.NICK_NAME).asString())
                .userType(getClaimByName(token, CommonConstant.USER_TYPE).asString())
                .userId(getClaimByName(token, CommonConstant.USER_ID).asLong())
                .build();
        AuthLoginContextHolder.saveLoginUserInfo(loginInfo);

    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }

    /**
     * 通过载荷名字获取发行时间
     */
    public DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

    /**
     * 生成JWT Token并保存到数据库
     * 签发时间：现在
     * 有效期：30天
     * 载荷内容：用户名、用户类型、用户ID
     * 加密密钥：TOKEN_SECRET
     *
     * @param nickName 用户昵称
     * @param platform 平台类型（暂未使用）
     * @param userId 用户ID
     * @param userType 用户类型（USER/DRIVER）
     * @param id 暂未使用
     * @return JWT Token字符串
     */
    public String createToken(String nickName, String platform, Long userId, String userType, Long id) {
        // 设置过期时间：30天后
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DAY_OF_MONTH, 30);
        Date expiresDate = nowTime.getTime();
        Date issuedAt = new Date();

        // 构建登录信息并保存到上下文
        JwtUserLoginInfo loginInfo = JwtUserLoginInfo.builder()
                .nickName(nickName)
                .userType(userType)
                .userId(userId)
                .build();
        AuthLoginContextHolder.saveLoginUserInfo(loginInfo);

        // 生成Token
        String token = JWT.create()
                .withIssuedAt(issuedAt)              // 签发时间：当前时间
                .withExpiresAt(expiresDate)          // 过期时间：30天后
                .withClaim(CommonConstant.NICK_NAME, nickName)
                .withClaim(CommonConstant.USER_TYPE, userType)
                .withClaim(CommonConstant.USER_ID, userId)
                .sign(Algorithm.HMAC256(TOKEN_SECRET));  // 使用密钥加密

        // 保存Token到数据库（替代 Redis）
        saveTokenToDatabase(userId, userType, token, issuedAt, expiresDate);

        return token;
    }

    /**
     * 保存Token到数据库
     * 如果已存在则更新，不存在则新增
     *
     * @param userId 用户ID
     * @param clientType 客户端类型（USER/DRIVER）
     * @param token Token字符串
     * @param issuedAt 签发时间
     * @param expiresAt 过期时间
     */
    private void saveTokenToDatabase(Long userId, String clientType, String token, Date issuedAt, Date expiresAt) {
        try {
            // 转换时间格式
            LocalDateTime issuedAtTime = issuedAt.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime expireTime = expiresAt.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            // 查询数据库中是否已存在该用户的Token记录
            Token existingToken = tokenRepository.getByUserIdAndClientType(userId, clientType);

            if (existingToken != null) {
                // 更新现有Token
                existingToken.setToken(token);
                existingToken.setIssuedAt(issuedAtTime);
                existingToken.setExpireTime(expireTime);
                existingToken.setModifier(userId.toString());
                tokenRepository.updateById(existingToken);
                log.info("更新用户Token到数据库 - 用户ID: {}, 客户端类型: {}", userId, clientType);
            } else {
                // 新增Token记录
                Token newToken = new Token();
                newToken.setUserId(userId);
                newToken.setClientType(clientType);
                newToken.setToken(token);
                newToken.setIssuedAt(issuedAtTime);
                newToken.setExpireTime(expireTime);
                tokenRepository.save(newToken);
                log.info("新增用户Token到数据库 - 用户ID: {}, 客户端类型: {}", userId, clientType);
            }
        } catch (Exception e) {
            log.error("保存Token到数据库失败 - 用户ID: {}, 客户端类型: {}", userId, clientType, e);
            // 不抛出异常，避免影响登录流程
        }
    }

}
