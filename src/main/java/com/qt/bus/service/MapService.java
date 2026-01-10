package com.qt.bus.service;

import com.qt.bus.dto.MapLocationRequest;
import com.qt.bus.dto.MapLocationResponse;

/**
 * 地图服务接口
 */
public interface MapService {

    /**
     * 获取地图位置信息
     * 根据用户类型返回不同数据：
     * - 司机：所有司机位置 + 所有乘客位置
     * - 乘客：所有司机位置 + 自己的位置
     *
     * @param request 当前用户位置信息
     * @return 地图位置数据
     */
    MapLocationResponse getMapLocations(MapLocationRequest request);
}
