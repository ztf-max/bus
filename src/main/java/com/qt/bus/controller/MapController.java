package com.qt.bus.controller;

import com.qt.bus.dto.MapLocationRequest;
import com.qt.bus.dto.MapLocationResponse;
import com.qt.bus.model.response.Result;
import com.qt.bus.service.MapService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 请求示例：
     * {
     *   "latitude": 31.230416,
     *   "longitude": 121.473701,
     *   "heading": 90.5,     // 可选，司机端提供
     *   "speed": 45.2        // 可选，司机端提供
     * }
     * 
     * @param request 当前用户位置信息
     * @return 地图上所有位置数据
     */
    @PostMapping("/locations")
    public Result<MapLocationResponse> getMapLocations(@RequestBody MapLocationRequest request) {
        MapLocationResponse response = mapService.getMapLocations(request);
        return Result.success(response);
    }
}
