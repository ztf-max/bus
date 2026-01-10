package com.qt.bus.service.impl;

import com.qt.bus.dao.model.User;
import com.qt.bus.dao.model.UserLocation;
import com.qt.bus.dao.repository.UserLocationRepository;
import com.qt.bus.dao.repository.UserRepository;
import com.qt.bus.dto.*;
import com.qt.bus.exception.SystemException;
import com.qt.bus.jwt.AuthLoginContextHolder;
import com.qt.bus.service.LocationService;
import com.qt.bus.service.MapService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地图服务实现类
 */
@Slf4j
@Service
public class MapServiceImpl implements MapService {

    @Resource
    private UserLocationRepository userLocationRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private LocationService locationService;

    /**
     * 用户类型：司机
     */
    private static final String USER_TYPE_DRIVER = "driver";

    /**
     * 用户类型：乘客
     */
    private static final String USER_TYPE_USER = "user";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MapLocationResponse getMapLocations(MapLocationRequest request) {
        // 1. 获取当前用户信息
        Long currentUserId = AuthLoginContextHolder.getLoginUserId();
        String userType = AuthLoginContextHolder.getLoginUserType();

       
        log.info("获取地图位置 - 用户ID: {}, 用户类型: {}", currentUserId, userType);

        // 2. 先更新当前用户的位置
        updateCurrentUserLocation(request);

        // 3. 根据用户类型返回不同数据
        if (USER_TYPE_DRIVER.equalsIgnoreCase(userType)) {
            // 司机端：返回所有司机 + 所有乘客
            return getDriverMapView(currentUserId);
        } else {
            // 乘客端：返回所有司机 + 自己
            return getUserMapView(currentUserId);
        }
    }

    /**
     * 更新当前用户位置
     */
    private void updateCurrentUserLocation(MapLocationRequest request) {
        if (request.getLatitude() != null && request.getLongitude() != null) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setLatitude(request.getLatitude());
            locationRequest.setLongitude(request.getLongitude());
            locationRequest.setHeading(request.getHeading());
            locationRequest.setSpeed(request.getSpeed());
            locationService.reportLocation(locationRequest);
        }
    }

    /**
     * 获取司机端地图视图
     * 返回：所有司机位置 + 所有乘客位置
     */
    private MapLocationResponse getDriverMapView(Long currentDriverId) {
        // 1. 查询所有司机位置
        List<DriverLocationVO> drivers = getAllDriverLocations();

        // 2. 查询所有乘客位置
        List<UserLocationVO> users = getAllUserLocations();

        log.info("司机端地图数据 - 司机数: {}, 乘客数: {}", drivers.size(), users.size());

        return MapLocationResponse.builder()
                .drivers(drivers)
                .users(users)
                .userType(USER_TYPE_DRIVER)
                .build();
    }

    /**
     * 获取乘客端地图视图
     * 返回：所有司机位置 + 当前乘客自己的位置
     */
    private MapLocationResponse getUserMapView(Long currentUserId) {
        // 1. 查询所有司机位置
        List<DriverLocationVO> drivers = getAllDriverLocations();

        // 2. 查询当前乘客自己的位置
        List<UserLocationVO> users = getCurrentUserLocation(currentUserId);

        log.info("乘客端地图数据 - 司机数: {}, 当前用户位置: {}", 
                drivers.size(), users.isEmpty() ? "未上报" : "已上报");

        return MapLocationResponse.builder()
                .drivers(drivers)
                .users(users)
                .userType(USER_TYPE_USER)
                .build();
    }

    /**
     * 获取所有司机位置信息
     */
    private List<DriverLocationVO> getAllDriverLocations() {
        // 1. 查询所有司机用户
        List<User> drivers = userRepository.listAllDrivers();
        if (drivers.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 获取司机ID列表
        List<Long> driverIds = drivers.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // 3. 批量查询司机位置
        List<UserLocation> locations = userLocationRepository.listByUserIds(driverIds);
        Map<Long, UserLocation> locationMap = locations.stream()
                .collect(Collectors.toMap(UserLocation::getUserId, loc -> loc));

        // 4. 组装数据（只返回有位置信息的司机）
        return drivers.stream()
                .filter(driver -> locationMap.containsKey(driver.getId()))
                .map(driver -> {
                    UserLocation location = locationMap.get(driver.getId());
                    return DriverLocationVO.builder()
                            .driverId(driver.getId())
                            .name(driver.getRealName() != null ? driver.getRealName() : driver.getNickname())
                            .plateNumber(driver.getPlateNumber() != null ? driver.getPlateNumber() : "")
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .heading(location.getHeading())
                            .speed(location.getSpeed())
                            .status(driver.getWorkStatus() != null ? driver.getWorkStatus() : 0)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取所有乘客位置信息
     */
    private List<UserLocationVO> getAllUserLocations() {
        // 1. 查询所有乘客用户
        List<User> users = userRepository.listAllUsers();
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 获取乘客ID列表
        List<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        // 3. 批量查询乘客位置
        List<UserLocation> locations = userLocationRepository.listByUserIds(userIds);
        Map<Long, UserLocation> locationMap = locations.stream()
                .collect(Collectors.toMap(UserLocation::getUserId, loc -> loc));

        // 4. 组装数据（只返回有位置信息的乘客）
        return users.stream()
                .filter(user -> locationMap.containsKey(user.getId()))
                .map(user -> {
                    UserLocation location = locationMap.get(user.getId());
                    return UserLocationVO.builder()
                            .userId(user.getId())
                            .nickname(user.getNickname() != null ? user.getNickname() : "微信用户")
                            .avatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "")
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取当前乘客的位置信息
     */
    private List<UserLocationVO> getCurrentUserLocation(Long userId) {
        UserLocation location = userLocationRepository.getByUserId(userId);
        if (location == null) {
            return Collections.emptyList();
        }

        User user = userRepository.getById(userId);
        UserLocationVO userLocationVO = UserLocationVO.builder()
                .userId(userId)
                .nickname(user != null ? user.getNickname() : "我")
                .avatarUrl(user != null ? user.getAvatarUrl() : "")
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        List<UserLocationVO> result = new ArrayList<>();
        result.add(userLocationVO);
        return result;
    }
}
