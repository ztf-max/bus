package com.qt.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 地图位置信息响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapLocationResponse {

    /**
     * 司机位置列表
     */
    private List<DriverLocationVO> drivers;

    /**
     * 乘客位置列表（仅司机端返回所有乘客，乘客端返回自己）
     */
    private List<UserLocationVO> users;

    /**
     * 当前用户类型：USER-乘客, DRIVER-司机
     */
    private String userType;
}
