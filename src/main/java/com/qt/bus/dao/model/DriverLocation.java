package com.qt.bus.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 司机实时位置表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("driver_locations")
public class DriverLocation extends BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联司机表ID
     */
    private Long driverId;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 车头朝向(0-360)
     */
    private Float heading;

    /**
     * 速度
     */
    private Float speed;
}
