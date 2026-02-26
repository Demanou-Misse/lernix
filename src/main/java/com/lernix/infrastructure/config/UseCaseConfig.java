package com.lernix.infrastructure.config;

import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.application.usecase.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordHasher passwordHasher) {
        return new CreateUserUseCase(userRepositoryPort, passwordHasher);
    }
}

