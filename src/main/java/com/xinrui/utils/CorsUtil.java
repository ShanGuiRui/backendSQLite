package com.xinrui.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 跨域配置工具类
 * 用于配置CORS（跨域资源共享）策略，解决前端应用与后端服务之间的跨域问题
 */
@Configuration
public class CorsUtil {

    /**
     * 配置CORS源
     * @return UrlBasedCorsConfigurationSource CORS配置源对象
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        // 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 允许访问的域名
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
        // 配置为"*"表示允许所有类型的请求头
        config.setAllowedHeaders(Arrays.asList("*"));
        // 配置为"*"表示允许所有HTTP方法（GET, POST, PUT, DELETE等）
        config.setAllowedMethods(Arrays.asList("*"));
        // 设置为true表示允许跨域请求携带认证信息（如cookie）
        config.setAllowCredentials(true);

        // 创建CORS配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 注册CORS配置，"/**"表示对所有路径生效
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * 创建CORS过滤器
     * @return CorsFilter CORS过滤器实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 使用已配置的CORS源创建过滤器
        return new CorsFilter(corsConfigurationSource());
    }
}