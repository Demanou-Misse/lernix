package com.lernix.domain.model;

import com.lernix.domain.enums.UserStatus;

import java.time.Instant;
import java.util.Objects;

/**
 * User Aggregate Root - Refined version 2026.
 * Using Value Objects for Email and PasswordHash to ensure domain integrity.
 */
public record User(
        UserId id,
        Email email,
        PasswordHash passwordHash,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt,
        Long version
) {
    public User {
        Objects.requireNonNull(id, "User ID cannot be null");
        Objects.requireNonNull(email, "Email object cannot be null");
        Objects.requireNonNull(passwordHash, "PasswordHash object cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
        Objects.requireNonNull(createdAt, "Creation date cannot be null");
        Objects.requireNonNull(updatedAt, "Update date cannot be null");
    }

    /**
     * Factory method for initial registration (Issue #2 & #5 compliant).
     */
    public static User register(Email email, PasswordHash passwordHash) {
        Instant now = Instant.now();
        return new User(
                UserId.generate(),
                email,
                passwordHash,
                UserStatus.ACTIVE,
                now,
                now,
                null
        );
    }

    /**
     * Domain Logic: Produces a new instance with a fresh password hash.
     */
    public User changePassword(PasswordHash newPasswordHash) {
        return new User(
                this.id,
                this.email,
                newPasswordHash,
                this.status,
                this.createdAt,
                Instant.now(),
                this.version
        );
    }

    /**
     * Domain Logic: Transition to DISABLED state (Soft Delete).
     */
    public User disable() {
        return new User(
                this.id,
                this.email,
                this.passwordHash,
                UserStatus.DISABLED,
                this.createdAt,
                Instant.now(),
                this.version
        );
    }
}


