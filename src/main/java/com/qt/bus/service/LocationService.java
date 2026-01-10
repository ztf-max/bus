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
     * @param userId   用户ID
     * @param userType 用户类型（user/driver）
     * @param userName 用户名称（用于修改人/创建人）
     * @param request  位置信息
     */
    void reportLocation(Long userId, String userType, String userName, LocationRequest request);
}
