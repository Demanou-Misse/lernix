package com.lernix.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Required for POST requests in development
                .authorizeHttpRequests(auth -> auth
                        // 1. Permit Swagger & OpenAPI Docs
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 2. Permit User Registration (Issue #2)
                        .requestMatchers("/api/v1/users/**").permitAll()
                        // 3. Secure everything else
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

