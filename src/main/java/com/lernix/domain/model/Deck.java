package com.lernix.domain.model;

import com.lernix.domain.enums.DeckStatus;

import java.time.Instant;

/**
 * Deck Aggregate Root.
 * Represents a collection of flashcards owned by a specific user.
 */
public record Deck(
        DeckId id,
        UserId ownerId,
        DeckTitle title,
        String description,
        DeckStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public Deck {
        if (ownerId == null) throw new IllegalArgumentException("Deck must have an owner");
        if (title == null) throw new IllegalArgumentException("Deck must have a title");
        if (status == null) throw new IllegalArgumentException("Deck status is required");
    }

    /**
     * Professional Factory Method for creating a new Deck.
     * Default state is ACTIVE upon creation.
     */
    public static Deck create(UserId ownerId, DeckTitle title, String description) {
        Instant now = Instant.now();
        return new Deck(
                DeckId.generate(),
                ownerId,
                title,
                description != null ? description.trim() : "",
                DeckStatus.ACTIVE,
                now,
                now
        );
    }

    // Business logic: Method to archive a deck
    public Deck archive() {
        return new Deck(id, ownerId, title, description, DeckStatus.ARCHIVED, createdAt, Instant.now());
    }
}

