package com.icebreaker.be.global.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:5173",
            "https://localhost:8080"
    );

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowedOrigins(ALLOWED_ORIGINS.toArray(new String[0]))
                .allowCredentials(true)
                .maxAge(3600);
    }
}
