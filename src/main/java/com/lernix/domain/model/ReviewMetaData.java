package com.lernix.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable snapshot of a Card's repetition statistics.
 * This Record encapsulates the mathematical state used by the Spaced Repetition Engine.
 *
 * @param easeFactor   The multiplier determining how fast the interval grows (Default: 2.5).
 * @param interval     The current delay in days until the next review.
 * @param repetitions  The total number of successful consecutive reviews.
 * @param nextReview   The precise point in time for the next scheduled study session.
 */
public record ReviewMetaData(
        double easeFactor,
        int interval,
        int repetitions,
        Instant nextReview
) {
    /**
     * Compact constructor for business invariant validation.
     * Ensures we never have corrupted data (e.g., negative intervals or sub-minimal Ease Factor).
     */
    public ReviewMetaData {
        Objects.requireNonNull(nextReview, "Next review timestamp cannot be null");

        if (easeFactor < 1.3) {
            throw new IllegalArgumentException("Ease Factor cannot be below the 1.3 floor to prevent 'Ease Hell'.");
        }
        if (interval < 0) {
            throw new IllegalArgumentException("Interval cannot be negative.");
        }
        if (repetitions < 0) {
            throw new IllegalArgumentException("Repetitions count cannot be negative.");
        }
    }

    /**
     * Factory method for creating the initial state of a NEW card.
     * Centralizing the 'default' state prevents logic leaks across the application.
     *
     * @return A fresh ReviewMetaData instance with system defaults.
     */
    public static ReviewMetaData createInitial() {
        return new ReviewMetaData(2.5, 0, 0, Instant.now());
    }
}
