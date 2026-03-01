package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    @DisplayName("Should create an ACTIVE deck via factory method")
    void shouldCreateActiveDeck() {
        UserId ownerId = new UserId(UUID.randomUUID());
        DeckTitle title = new DeckTitle("Biology 101");

        Deck deck = Deck.create(ownerId, title, "Basic biology cards");

        assertNotNull(deck.id());
        assertEquals(DeckStatus.ACTIVE, deck.status());
        assertEquals(ownerId, deck.ownerId());
        assertNotNull(deck.createdAt());
        assertEquals(deck.createdAt(), deck.updatedAt());
    }

    @Test
    @DisplayName("Should produce a new ARCHIVED instance when archiving")
    void shouldArchiveDeck() {
        Deck activeDeck = Deck.create(new UserId(UUID.randomUUID()), new DeckTitle("Chemistry"), "");

        Deck archivedDeck = activeDeck.archive();

        assertEquals(DeckStatus.ARCHIVED, archivedDeck.status());
        assertEquals(activeDeck.id(), archivedDeck.id()); // Same identity
        assertTrue(archivedDeck.updatedAt().isAfter(activeDeck.updatedAt())
                || archivedDeck.updatedAt().equals(activeDeck.updatedAt()));
    }
}

