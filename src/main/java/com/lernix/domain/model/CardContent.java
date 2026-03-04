package com.lernix.domain.model;

/**
 * Value Object representing the learning content of a Card.
 * Ensures that both Front and Back sides are properly populated.
 */
public record CardContent(String front, String back) {
    public CardContent {
        if (front == null || front.isBlank()) {
            throw new IllegalArgumentException("Card front side cannot be empty or null");
        }
        if (back == null || back.isBlank()) {
            throw new IllegalArgumentException("Card back side cannot be empty or null");
        }

        // Sanitize inputs to prevent hidden whitespace issues
        front = front.trim();
        back = back.trim();
    }
}

