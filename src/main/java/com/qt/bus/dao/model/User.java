package com.qt.bus.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户/司机综合表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("users")
public class User extends BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 微信OpenID
     */
    private String openid;

    /**
     * 用户类型: user-乘客, driver-司机
     */
    private String userType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 车牌号（司机专用）
     */
    private String plateNumber;

    /**
     * 车辆描述（司机专用，如：白色·丰田卡罗拉）
     */
    private String vehicleDesc;

    /**
     * 司机工作状态: 0-收车, 1-听单中, 2-服务中
     */
    @TableField("work_status")
    private Integer workStatus;
}
