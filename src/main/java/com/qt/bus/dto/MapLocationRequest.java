package com.qt.bus.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 地图位置查询请求DTO
 */
@Data
public class MapLocationRequest {

    /**
     * 当前用户纬度
     */
    private BigDecimal latitude;

    /**
     * 当前用户经度
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
