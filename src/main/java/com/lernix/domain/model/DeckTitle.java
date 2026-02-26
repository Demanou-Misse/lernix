package com.lernix.domain.model;

public record DeckTitle(String value) {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public DeckTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Deck title cannot be empty");
        }
        if (value.trim().length() < MIN_LENGTH || value.trim().length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Deck title must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }
}

