package com.qt.bus.config;

import com.qt.bus.interceptor.RequestInterceptor;
import com.qt.bus.interceptor.TokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Resource
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // token拦截器
        registry.addInterceptor(tokenInterceptor)
                // 拦截
                .addPathPatterns("/**")
                // 放行
                .excludePathPatterns("/error")
                .excludePathPatterns("/health")
                .excludePathPatterns("/test/**")
                .order(1);

        // Trace拦截器
        registry.addInterceptor(requestInterceptor).order(2);


    }
}
