package com.fnf.incident.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")        // /api로 시작하는 모든 통로에 대해
                .allowedOrigins("*")           // 어떤 화면에서 불러도 허용
                .allowedMethods("*");          // GET, POST, DELETE 등 모두 허용
    }
}