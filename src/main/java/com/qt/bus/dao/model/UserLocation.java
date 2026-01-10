package com.qt.bus.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 用户实时位置表实体类（包含乘客和司机）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_locations")
public class UserLocation extends BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联用户表ID
     */
    private Long userId;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 车头朝向(0-360)，司机专用
     */
    private Float heading;

    /**
     * 速度，司机专用
     */
    private Float speed;
}
