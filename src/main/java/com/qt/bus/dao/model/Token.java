package com.qt.bus.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 用户Token在线状态表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("token")
public class Token extends BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 客户端类型(WEB/TEAM)
     */
    private String clientType;

    /**
     * Token字符串
     */
    private String token;

    /**
     * Token签发时间
     */
    private LocalDateTime issuedAt;

    /**
     * Token过期时间
     */
    private LocalDateTime expireTime;
}
