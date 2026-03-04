package com.lernix.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate Root representing a Flashcard.
 * Anemic models are avoided by enforcing invariants in the constructor.
 */
public record Card(
        CardId id,
        DeckId deckId,      // Mandatory ownership link
        CardContent content,
        Instant createdAt,
        Instant updatedAt
) {
    public Card {
        Objects.requireNonNull(id, "Id is required");
        Objects.requireNonNull(deckId, "Deck ownership is required");
        Objects.requireNonNull(content, "Card content is required");
        Objects.requireNonNull(createdAt, "Creation timestamp is required");
        Objects.requireNonNull(updatedAt, "Update timestamp is required");
    }

    public static Card create(DeckId deckId, String front, String back) {
        Instant now = Instant.now();
        return new Card(
                CardId.generate(),
                deckId,
                new CardContent(front, back),
                now,
                now
        );
    }

    public Card updateContent(String newFront, String newBack) {
        return new Card(
                this.id,
                this.deckId,
                new CardContent(newFront, newBack),
                this.createdAt,
                Instant.now()
        );
    }
}

