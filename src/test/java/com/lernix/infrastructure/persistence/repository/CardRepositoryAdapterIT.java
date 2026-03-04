package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CardRepositoryAdapter.class)
@DisplayName("Infrastructure: CardRepository Integration Tests")
class CardRepositoryAdapterIT {

    private final CardRepositoryAdapter cardRepositoryAdapter;
    private final EntityManager entityManager;

    @Autowired
    public CardRepositoryAdapterIT(CardRepositoryAdapter cardRepositoryAdapter, EntityManager entityManager) {
        this.cardRepositoryAdapter = cardRepositoryAdapter;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Should persist card and verify Cascade Delete when Deck is removed")
    void shouldPersistAndHandleCascadeDelete() {
        // 1. Given: A persisted User and Deck
        UUID userId = UUID.randomUUID();
        UserEntity owner = UserEntity.builder().id(userId).email("it-card-"+userId+"@test.com").passwordHash("h").createdAt(Instant.now()).build();
        entityManager.persist(owner);

        UUID deckId = UUID.randomUUID();
        DeckEntity deck = DeckEntity.builder().id(deckId).title("IT Deck").status("ACTIVE").owner(owner).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        entityManager.persist(deck);
        entityManager.flush();

        // 2. When: Saving a card
        Card card = Card.create(new DeckId(deckId), "Front SQL", "Back SQL");
        cardRepositoryAdapter.save(card);
        entityManager.flush();
        entityManager.clear();

        // 3. Then: Verify existence and Cascade
        assertTrue(cardRepositoryAdapter.existsById(new CardId(card.id().value())));

        // Delete Deck to trigger SQL Cascade
        entityManager.remove(entityManager.find(DeckEntity.class, deckId));
        entityManager.flush();

        assertFalse(cardRepositoryAdapter.existsById(new CardId(card.id().value())), "Card should be deleted by database cascade");
    }
}

