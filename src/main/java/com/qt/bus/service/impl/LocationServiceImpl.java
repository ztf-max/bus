package com.qt.bus.service.impl;

import com.qt.bus.dao.model.UserLocation;
import com.qt.bus.dao.repository.UserLocationRepository;
import com.qt.bus.dto.LocationRequest;
import com.qt.bus.jwt.AuthLoginContextHolder;
import com.qt.bus.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 位置服务实现类
 */
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportLocation(LocationRequest request) {
        // 从登录上下文获取用户信息
        Long userId = AuthLoginContextHolder.getLoginUserId();
        String userType = AuthLoginContextHolder.getLoginUserType();
        String userName = AuthLoginContextHolder.getLoginUserName();

        if (userId == null) {
            return;
        }

        log.info("位置上报 - 用户ID: {}, 用户类型: {}, 经纬度: ({}, {})", 
                userId, userType, request.getLatitude(), request.getLongitude());

        // 更新用户位置（司机和乘客都存在同一张表）
        updateUserLocation(userId, userName, request);
    }

    /**
     * 更新用户位置（包括乘客和司机）
     */
    private void updateUserLocation(Long userId, String userName, LocationRequest request) {
        // 查询是否已存在该用户的位置记录
        UserLocation existingLocation = userLocationRepository.getByUserId(userId);

        if (existingLocation != null) {
            // 更新现有记录
            existingLocation.setLatitude(request.getLatitude());
            existingLocation.setLongitude(request.getLongitude());
            existingLocation.setHeading(request.getHeading());
            existingLocation.setSpeed(request.getSpeed());
            existingLocation.setModifier(userName);
            userLocationRepository.updateById(existingLocation);
            log.info("更新用户位置 - 用户ID: {}", userId);
        } else {
            // 新增记录
            UserLocation userLocation = new UserLocation();
            userLocation.setUserId(userId);
            userLocation.setLatitude(request.getLatitude());
            userLocation.setLongitude(request.getLongitude());
            userLocation.setHeading(request.getHeading());
            userLocation.setSpeed(request.getSpeed());
            userLocation.setCreator(userName);
            userLocation.setModifier(userName);
            userLocationRepository.save(userLocation);
            log.info("新增用户位置 - 用户ID: {}", userId);
        }
    }
}
