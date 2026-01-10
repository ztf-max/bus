package com.qt.bus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查 不要删除！！！
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "success";
    }
}
