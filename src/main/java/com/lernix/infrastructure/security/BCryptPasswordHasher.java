package com.lernix.infrastructure.security;

import com.lernix.domain.model.PasswordHash;
import com.lernix.domain.service.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Infrastructure adapter for password hashing.
 * Uses Spring Security's PasswordEncoder to fulfill the domain contract.
 */
@Component
@RequiredArgsConstructor
public class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public PasswordHash encode(String plainPassword) {
        // We wrap the String result into our Domain Value Object
        return new PasswordHash(passwordEncoder.encode(plainPassword));
    }

    @Override
    public boolean matches(String plainPassword, PasswordHash hash) {
        return passwordEncoder.matches(plainPassword, hash.value());
    }
}

