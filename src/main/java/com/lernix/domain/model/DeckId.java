package com.lernix.domain.model;

import java.util.UUID;

public record DeckId(UUID value) {
    public DeckId {
        if (value == null) {
            throw new IllegalArgumentException("Deck ID cannot be null");
        }
    }

    public static DeckId generate() {
        return new DeckId(UUID.randomUUID());
    }
}

