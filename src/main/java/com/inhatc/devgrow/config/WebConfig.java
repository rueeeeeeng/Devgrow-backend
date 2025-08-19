package com.inhatc.devgrow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @desc REST API의 CORS configuration 역할
 */
@Configuration
@EnableWebMvc
//@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Include POST for signup
                .allowedHeaders("*")
                .allowCredentials(true);

        /*CorsConfiguration config = new CorsConfiguration();
        config.addExposedHeader("Authorization");*/
    }
}