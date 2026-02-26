package com.lernix.domain.model;

public record PasswordHash(String value) {
    public PasswordHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty");
        }

        if (value.length() < 30) {
            throw new IllegalArgumentException("Password hash seems too short or is in plain text");
        }
    }
}
