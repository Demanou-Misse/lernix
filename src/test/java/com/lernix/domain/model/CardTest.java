package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain: Card Aggregate Unit Tests")
class CardTest {

    @Test
    @DisplayName("Should create a card with initial timestamps via factory")
    void shouldCreateCardCorrectly() {
        DeckId deckId = new DeckId(UUID.randomUUID());

        Card card = Card.create(deckId, "Question", "Answer");

        assertAll("Card initial state",
                () -> assertNotNull(card.id()),
                () -> assertEquals(deckId, card.deckId()),
                () -> assertEquals("Question", card.content().front()),
                () -> assertNotNull(card.createdAt()),
                () -> assertEquals(card.createdAt(), card.updatedAt(), "Initially, createdAt and updatedAt should be identical")
        );
    }

    @Test
    @DisplayName("Should produce a new instance with updated timestamp when content changes")
    void shouldUpdateContentImmutably() throws InterruptedException {
        Card original = Card.create(new DeckId(UUID.randomUUID()), "Old Front", "Old Back");

        Thread.sleep(1);

        Card updated = original.updateContent("New Front", "New Back");

        assertAll("Updated card state",
                () -> assertEquals(original.id(), updated.id(), "Identity must be preserved"),
                () -> assertEquals("New Front", updated.content().front()),
                () -> assertEquals(original.createdAt(), updated.createdAt(), "Creation date must never change"),
                () -> assertTrue(updated.updatedAt().isAfter(original.updatedAt()), "Update date must be refreshed")
        );
    }
}

