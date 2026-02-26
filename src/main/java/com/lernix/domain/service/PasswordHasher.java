package com.lernix.domain.service;

import com.lernix.domain.model.PasswordHash;

public interface PasswordHasher {
    PasswordHash encode(String plainPassword);
    boolean matches(String plainPassword, PasswordHash hash);
}

