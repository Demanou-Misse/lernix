package com.lernix.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Enterprise Security Configuration.
 * Centralized authorization rules for User Account Management.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disabled for stateless API (POST/PATCH/DELETE)
                .authorizeHttpRequests(auth -> auth
                        // 1. Documentation & Public discovery
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. User Registration (The only public user endpoint)
                        .requestMatchers("/api/v1/users/register").permitAll()

                        // 3. User Account Management (Sensitive - logic will be strictly enforced in Issue #8)
                        .requestMatchers("/api/v1/users/{id}/**").permitAll()

                        // 4. Content Management
                        .requestMatchers("/api/v1/decks/**").permitAll()
                        .requestMatchers("/api/v1/cards/**").permitAll()

                        // 5. Default Security Policy
                        .anyRequest().authenticated()
                )
                .build();
    }
}


