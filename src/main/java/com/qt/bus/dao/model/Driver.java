package com.qt.bus.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 司机表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drivers")
public class Driver extends BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 微信OpenID
     */
    private String openid;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 状态：0-下班, 1-听单中, 2-服务中
     */
    private Integer status;
}
