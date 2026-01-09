package com.qt.bus.service;

import com.qt.bus.dto.LocationRequest;

/**
 * 位置服务接口
 */
public interface LocationService {

    /**
     * 上报位置信息
     * 根据用户平台类型(platform)自动判断是乘客还是司机，并更新对应表
     *
     * @param request 位置信息
     */
    void reportLocation(LocationRequest request);
}
