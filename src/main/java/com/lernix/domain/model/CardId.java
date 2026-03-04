package com.lernix.domain.model;

import java.util.Objects;
import java.util.UUID;


public record CardId(UUID value) {
    public CardId {
        Objects.requireNonNull(value, "Card ID value cannot be null");
    }

    public static CardId generate() {
        return new CardId(UUID.randomUUID());
    }
}

