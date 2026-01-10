package com.qt.bus.controller;

import com.qt.bus.dto.MapLocationResponse;
import com.qt.bus.jwt.AuthLoginContextHolder;
import com.qt.bus.model.response.Result;
import com.qt.bus.service.MapService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地图控制器
 */
@Slf4j
@RestController
@RequestMapping("/map")
public class MapController {

    @Resource
    private MapService mapService;

    /**
     * 获取地图位置信息
     * 
     * 功能说明：
     * - 司机端：返回所有司机位置 + 所有乘客位置
     * - 乘客端：返回所有司机位置 + 自己的位置
     * 
     * 注意：本接口仅用于查询，位置上报请调用 /location/report 接口
     * 
     * @return 地图上所有位置数据
     */
    @GetMapping("/locations")
    public Result<MapLocationResponse> getMapLocations() {
        Long userId = AuthLoginContextHolder.getLoginUserId();
        String userType = AuthLoginContextHolder.getLoginUserType();
        
        MapLocationResponse response = mapService.getMapLocations(userId, userType);
        return Result.success(response);
    }
}
