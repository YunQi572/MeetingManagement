package com.zhku.meeting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 允许跨域的源
        config.addAllowedOrigin("http://localhost:4002");
        config.addAllowedOrigin("http://localhost:4000");
        config.addAllowedOrigin("http://localhost:4001");
        config.addAllowedOrigin("http://127.0.0.1:4000");
        config.addAllowedOrigin("http://127.0.0.1:4001");
        config.addAllowedOrigin("http://127.0.0.1:4002");
        config.addAllowedOrigin("http://localhost:9876");
        config.addAllowedOrigin("http://localhost:9877");
        // 允许跨域的请求头
        config.addAllowedHeader("*");
        // 允许跨域的请求方法
        config.addAllowedMethod("*");
        // 允许携带cookie
        config.setAllowCredentials(true);
        // 暴露响应头
        config.addExposedHeader("*");
        // 设置预检请求的缓存时间
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}