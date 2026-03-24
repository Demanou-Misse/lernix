package com.lernix.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a Card Tag.
 * Self-validating immutable record.
 * Ensures data consistency across the entire Spaced Repetition System.
 */
public record Tag(String value) {

    // Business Rules for Tags: Lowercase, alphanumeric, no spaces, 2-20 chars.
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-z0-9-]{2,20}$");

    /**
     * Canonical constructor with strict business invariants.
     */
    public Tag {
        Objects.requireNonNull(value, "Tag value cannot be null");

        // Normalization: Remove whitespace and convert to lowercase
        value = value.trim().toLowerCase();

        if (!VALID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid Tag format [%s]. Tags must be 2-20 characters, alphanumeric or hyphens only."
                            .formatted(value)
            );
        }
    }

    /**
     * Static factory for cleaner API usage.
     */
    public static Tag of(String value) {
        return new Tag(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

