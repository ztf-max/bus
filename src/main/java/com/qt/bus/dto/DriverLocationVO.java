package com.qt.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 司机位置信息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLocationVO {

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 司机姓名
     */
    private String name;

    /**
     * 车牌号
     */
    private String plateNumber;

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

    /**
     * 状态: 0-下班, 1-听单中, 2-服务中
     */
    private Integer status;
}
