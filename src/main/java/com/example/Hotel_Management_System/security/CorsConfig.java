package com.example.Hotel_Management_System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

                                                // This method creates a WebMvcConfigurer bean
                                                // Used to customize Spring MVC behavior (CORS here)
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {

                                                // Anonymous class implementing WebMvcConfigurer
        return new WebMvcConfigurer() {

            // Configure CORS rules
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                registry.addMapping("/**")// Allow CORS for all endpoints
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow these HTTP methods
                        .allowedHeaders("*");// Allow all request headers
            }
        };
    }
}
