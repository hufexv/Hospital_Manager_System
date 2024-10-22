package com.hospital.config;

import com.hospital.interceptors.JwtInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//这个配置类用于在Spring MVC中设置JWT拦截器，拦截特定路径的请求，同时排除一些不需要拦截的路径。这通常用于处理认证和授权等功能。

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/666")
                .excludePathPatterns("/patient/pdf")
                .excludePathPatterns("/**/login");//不能通过别的方式登录
    }
}
