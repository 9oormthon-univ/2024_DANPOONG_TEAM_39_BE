package com.example._2024_danpoong_team_39_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:8081", "http://192.168.219.100:8080")  // 실제 IP를 추가
                .allowedMethods("*");
    }
}
