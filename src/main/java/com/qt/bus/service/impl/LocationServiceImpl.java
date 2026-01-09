package com.qt.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qt.bus.dao.model.DriverLocation;
import com.qt.bus.dao.model.UserLocation;
import com.qt.bus.dao.repository.DriverLocationRepository;
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

    @Autowired
    private DriverLocationRepository driverLocationRepository;

    /**
     * 司机平台标识
     */
    private static final String PLATFORM_DRIVER = "DRIVER";

    /**
     * 乘客平台标识
     */
    private static final String PLATFORM_USER = "USER";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportLocation(LocationRequest request) {
        // 从登录上下文获取用户信息
        Long userId = AuthLoginContextHolder.getLoginUserId();
        String platform = AuthLoginContextHolder.getLoginUserPlatform();
        String userName = AuthLoginContextHolder.getLoginUserName();

        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        log.info("位置上报 - 用户ID: {}, 平台: {}, 经纬度: ({}, {})", 
                userId, platform, request.getLatitude(), request.getLongitude());

        // 根据平台类型区分处理
        if (PLATFORM_DRIVER.equalsIgnoreCase(platform)) {
            // 司机位置上报
            updateDriverLocation(userId, userName, request);
        } else if (PLATFORM_USER.equalsIgnoreCase(platform)) {
            // 乘客位置上报
            updateUserLocation(userId, userName, request);
        } else {
            // 默认按乘客处理
            updateUserLocation(userId, userName, request);
        }
    }

    /**
     * 更新乘客位置
     */
    private void updateUserLocation(Long userId, String userName, LocationRequest request) {
        // 查询是否已存在该用户的位置记录
        LambdaQueryWrapper<UserLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLocation::getUserId, userId);
        UserLocation existingLocation = userLocationRepository.getOne(queryWrapper);

        if (existingLocation != null) {
            // 更新现有记录
            existingLocation.setLatitude(request.getLatitude());
            existingLocation.setLongitude(request.getLongitude());
            existingLocation.setModifier(userName);
            userLocationRepository.updateById(existingLocation);
            log.info("更新乘客位置 - 用户ID: {}", userId);
        } else {
            // 新增记录
            UserLocation userLocation = new UserLocation();
            userLocation.setUserId(userId);
            userLocation.setLatitude(request.getLatitude());
            userLocation.setLongitude(request.getLongitude());
            userLocation.setCreator(userName);
            userLocation.setModifier(userName);
            userLocationRepository.save(userLocation);
            log.info("新增乘客位置 - 用户ID: {}", userId);
        }
    }

    /**
     * 更新司机位置
     */
    private void updateDriverLocation(Long driverId, String driverName, LocationRequest request) {
        // 查询是否已存在该司机的位置记录
        LambdaQueryWrapper<DriverLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DriverLocation::getDriverId, driverId);
        DriverLocation existingLocation = driverLocationRepository.getOne(queryWrapper);

        if (existingLocation != null) {
            // 更新现有记录
            existingLocation.setLatitude(request.getLatitude());
            existingLocation.setLongitude(request.getLongitude());
            existingLocation.setHeading(request.getHeading());
            existingLocation.setSpeed(request.getSpeed());
            existingLocation.setModifier(driverName);
            driverLocationRepository.updateById(existingLocation);
            log.info("更新司机位置 - 司机ID: {}", driverId);
        } else {
            // 新增记录
            DriverLocation driverLocation = new DriverLocation();
            driverLocation.setDriverId(driverId);
            driverLocation.setLatitude(request.getLatitude());
            driverLocation.setLongitude(request.getLongitude());
            driverLocation.setHeading(request.getHeading());
            driverLocation.setSpeed(request.getSpeed());
            driverLocation.setCreator(driverName);
            driverLocation.setModifier(driverName);
            driverLocationRepository.save(driverLocation);
            log.info("新增司机位置 - 司机ID: {}", driverId);
        }
    }
}
