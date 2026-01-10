package com.qt.bus.interceptor;

import com.qt.bus.dao.model.Token;
import com.qt.bus.dao.repository.TokenRepository;
import com.qt.bus.exception.SystemException;
import com.qt.bus.jwt.AuthLoginContextHolder;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final static String TOKEN = "token";
    
    /**
     * 客户端类型：WEB
     */
    private final static String CLIENT_TYPE_WEB = "WEB";
    
    /**
     * 客户端类型：TEAM
     */
    private final static String CLIENT_TYPE_TEAM = "TEAM";

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private TokenRepository tokenRepository;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response, 
                             @NonNull Object handler) {

        // 判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            // 请求头存在Token则进入Token校验
            verifyToken(request);
            return true;
        }
        //如果请求头不存在 Token，直接抛出无登录异常
        throw new SystemException("暂无登陆状态，请进行登录!", 401);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, 
                                @NonNull HttpServletResponse response, 
                                @NonNull Object handler, 
                                @Nullable Exception ex) {
        AuthLoginContextHolder.clear();
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Token 字段
     */
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(TOKEN);
        return token != null;
    }

    /**
     * 执行token校验
     */
    @Transactional(rollbackFor = Exception.class)
    protected void verifyToken(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(TOKEN);

        // 验证token有效性并解析用户信息
        jwtUtils.verify(token);

        Long userId = AuthLoginContextHolder.getLoginUserId();
        if (Objects.isNull(userId)) {
            return;
        }
     

        // 处理Web端token（默认）
        handleWebToken(userId, token);
    }

    /**
     * 处理Web端Token
     */
    private void handleWebToken(Long userId, String token) {
        // 查询数据库中的token记录
        Token dbToken = tokenRepository.getByUserIdAndClientType(userId, CLIENT_TYPE_WEB);

        // 解析当前token的签发时间和过期时间
        Date tokenIssuedAt = jwtUtils.decodeToken(token).getIssuedAt();
        Date tokenExpiresAt = jwtUtils.decodeToken(token).getExpiresAt();
        
        LocalDateTime issuedAtTime = tokenIssuedAt.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expireTime = tokenExpiresAt.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (dbToken == null) {
            // 数据库中不存在，新增token记录
            Token newToken = new Token();
            newToken.setUserId(userId);
            newToken.setClientType(CLIENT_TYPE_WEB);
            newToken.setToken(token);
            newToken.setIssuedAt(issuedAtTime);
            newToken.setExpireTime(expireTime);
            newToken.setCreator(userId.toString());
            newToken.setModifier(userId.toString());
            tokenRepository.save(newToken);
            log.info("新增Web端Token - 用户ID: {}", userId);
            return;
        }

        // 如果token相同，直接返回
        if (Objects.equals(dbToken.getToken(), token)) {
            return;
        }

        // 比较签发时间，新token签发时间更晚则更新，否则抛出异常
        Date dbTokenIssuedAt = Date.from(dbToken.getIssuedAt()
                .atZone(ZoneId.systemDefault()).toInstant());
        
        if (tokenIssuedAt.after(dbTokenIssuedAt)) {
            // 更新为新token
            dbToken.setToken(token);
            dbToken.setIssuedAt(issuedAtTime);
            dbToken.setExpireTime(expireTime);
            dbToken.setModifier(userId.toString());
            tokenRepository.updateById(dbToken);
            log.info("更新Web端Token - 用户ID: {}", userId);
        } else {
            throw new SystemException("账号已在其他设备登录！", 401);
        }
    }

}
