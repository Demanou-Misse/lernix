package com.lernix.domain.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Encapsulates the user feedback for a card review session.
 * Maps subjective recall quality to standardized numerical weights for algorithm processing.
 */
public enum ReviewGrade {

    /** Complete failure to recall information. */
    AGAIN(0, "Forgotten - Total failure to recognize the card"),

    /** Correct response but required significant mental effort. */
    HARD(1, "Difficult - Correct response with heavy hesitation"),

    /** Correct response after a normal thinking delay. */
    GOOD(3, "Correct - Standard recall speed"),

    /** Perfect, immediate recall without any doubt. */
    EASY(5, "Easy - Immediate and perfect recall");

    private final int value;
    private final String description;

    ReviewGrade(int value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * return The numerical weight used in SM2/FSRS calculations.
     */
    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Safe lookup method to convert a raw integer from the API/Database into a Grade.
     * Use this in your Controllers/Mappers to ensure Data Integrity.
     */
    public static Optional<ReviewGrade> fromValue(int value) {
        return Arrays.stream(values())
                .filter(grade -> grade.value == value)
                .findFirst();
    }
}

