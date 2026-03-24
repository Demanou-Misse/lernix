package com.lernix.domain.model;

/**
 * Represents the account lifecycle of a User.
 */
public enum UserStatus {
    /** Account is fully functional. */
    ACTIVE,

    /** Account is temporarily locked or self-disabled (Soft Delete). */
    DISABLED,

    /** Account created but email verification is pending. */
    PENDING,

    /** Account marked for permanent physical deletion (Compliance/GDPR). */
    DELETED
}

