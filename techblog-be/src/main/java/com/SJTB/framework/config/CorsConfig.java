package com.SJTB.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("${spring.config.allowedOrigin}")
    private String allowedOrigin;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 프론트단에서 호출하는 요청은 모두 허용하고 쿠키도 같이 받아오게함
        //config.addAllowedOrigin(allowedOrigin);

        // 로컬 환경 배열로 추가
        config.setAllowedOrigins(Arrays.asList(allowedOrigin, "http://localhost:3000"));

        // 특정 헤더를 명시적으로 추가
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Authorization");
        config.setExposedHeaders(Arrays.asList("Content-Type", "Authorization"));
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("OPTIONS");  // 프리플라이트 요청을 허용
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}