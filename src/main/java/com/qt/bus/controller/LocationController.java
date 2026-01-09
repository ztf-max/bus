package com.qt.bus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qt.bus.config.ApiResponse;
import com.qt.bus.dto.LocationRequest;
import com.qt.bus.service.LocationService;
import lombok.extern.slf4j.Slf4j;

/**
 * 位置上报控制器
 */
@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * 位置上报接口 乘客端和司机端共用此接口，根据token中的platform字段自动识别
     */
    @PostMapping("/report")
    public ApiResponse<Boolean> reportLocation(@RequestBody LocationRequest request) {
        locationService.reportLocation(request);
        return ApiResponse.ok(true);
    }
}
