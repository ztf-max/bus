package com.qt.bus.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 位置上报请求DTO
 */
@Data
public class LocationRequest {

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 车头朝向(0-360) - 仅司机端需要
     */
    private Float heading;

    /**
     * 速度 - 仅司机端需要
     */
    private Float speed;
}
