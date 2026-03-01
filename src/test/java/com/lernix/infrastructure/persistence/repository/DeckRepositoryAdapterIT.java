package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DeckRepositoryAdapter.class)
class DeckRepositoryAdapterIT {

    private final DeckRepositoryAdapter deckRepositoryAdapter;
    private final EntityManager entityManager;

    @Autowired
    public DeckRepositoryAdapterIT(DeckRepositoryAdapter deckRepositoryAdapter, EntityManager entityManager) {
        this.deckRepositoryAdapter = deckRepositoryAdapter;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Should persist and retrieve a deck with its owner relationship")
    void shouldPersistAndFindDeck() {
        // 1. Given: A unique persisted user
        UUID userId = UUID.randomUUID();
        UserEntity owner = UserEntity.builder()
                .id(userId)
                .email("it-test-" + userId + "@lernix.com")
                .passwordHash("hash")
                .createdAt(Instant.now())
                .build();

        entityManager.persist(owner);
        entityManager.flush();

        // 2. When: Saving a deck via the adapter
        Deck deck = Deck.create(new UserId(userId), new DeckTitle("Integration Test"), "Desc");
        deckRepositoryAdapter.save(deck);

        entityManager.flush();
        entityManager.clear();

        // 3. Then: Retrieval should be successful
        List<Deck> decks = deckRepositoryAdapter.findAllByOwnerId(new UserId(userId));

        assertAll("Database validation",
                () -> assertFalse(decks.isEmpty(), "The deck list should not be empty"),
                () -> assertEquals("Integration Test", decks.get(0).title().value()),
                () -> assertEquals(userId, decks.get(0).ownerId().value())
        );
    }

}



