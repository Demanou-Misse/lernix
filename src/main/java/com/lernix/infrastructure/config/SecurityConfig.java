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
        return http
                .csrf(AbstractHttpConfigurer::disable) // Required for POST/PATCH/DELETE
                .authorizeHttpRequests(auth -> auth
                        // 1. Swagger & OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 2. User Registration (Issue #2)
                        .requestMatchers("/api/v1/users/**").permitAll()
                        // 3. Deck Management (Issue #3) - ADD THIS LINE
                        .requestMatchers("/api/v1/decks/**").permitAll()
                        // 4. Everything else remains secured
                        .anyRequest().authenticated()
                )
                .build();
    }
}
