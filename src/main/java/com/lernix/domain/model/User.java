package com.lernix.domain.model;

import java.time.Instant;

public record User(
        UserId id,
        Email email,
        String passwordHash,
        Instant createdAt
) {
    public User {
        if (createdAt == null) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
    }

    public static User create(Email email, String passwordHash) {
        return new User(UserId.generate(), email, passwordHash, Instant.now());
    }
}
