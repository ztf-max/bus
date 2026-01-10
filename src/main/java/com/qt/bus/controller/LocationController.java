package com.qt.bus.controller;

import com.qt.bus.dto.LocationRequest;
import com.qt.bus.model.response.Result;
import com.qt.bus.service.LocationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 位置上报控制器
 */
@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {

    @Resource
    private LocationService locationService;

    /**
     * 位置上报接口 乘客端和司机端共用此接口，根据token中的platform字段自动识别
     */
    @PostMapping("/report")
    public Result<Boolean> reportLocation(@RequestBody LocationRequest request) {
        locationService.reportLocation(request);
        return Result.success(true);
    }
}
