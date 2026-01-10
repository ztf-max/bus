package com.qt.bus.interceptor;

import com.qt.bus.constants.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {

        // 兼容分布式traceId传递
        if (request.getHeader("Trace-Id") != null) {
            MDC.put(CommonConstant.TRACE_ID, request.getHeader("Trace-Id"));
        } else {
            MDC.put(CommonConstant.TRACE_ID, UUID.randomUUID().toString());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        MDC.remove(CommonConstant.TRACE_ID);
    }

}
