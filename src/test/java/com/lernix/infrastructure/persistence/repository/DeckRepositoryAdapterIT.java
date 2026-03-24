package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.infrastructure.persistence.entity.UserEntity;import com.lernix.infrastructure.web.mapper.DeckMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enterprise Integration Test for Deck Persistence.
 * Validates the full lifecycle: Domain -> Entity -> Database -> Domain.
 */
@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        DeckRepositoryAdapter.class,
        DeckMapper.class
})
@DisplayName("Infrastructure: DeckRepository Integration Tests")
class DeckRepositoryAdapterIT {

    @Autowired
    private DeckRepositoryAdapter deckRepositoryAdapter;

    @Autowired
    private TestEntityManager entityManager; // Best practice over raw EntityManager for testing

    @Test
    @DisplayName("Should persist and retrieve a deck with its owner relationship")
    void shouldPersistAndFindDeck() {
        // 1. Arrange: Persist a valid Owner first (referential integrity)
        UUID userId = UUID.randomUUID();
        UserEntity owner = UserEntity.builder()
                .id(userId)
                .email("it-test-" + userId + "@lernix.com")
                .passwordHash("hashed_password_dummy")
                .status("ACTIVE") // Mandatory field from your SQL schema
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        entityManager.persist(owner);
        entityManager.flush();

        // 2. Act: Create a Domain Deck and save it via the Adapter
        // Uses the Deck.create factory from your domain logic
        Deck domainDeck = Deck.create(
                new UserId(userId),
                new DeckTitle("Integration Test Deck"),
                "Description for the test"
        );

        deckRepositoryAdapter.save(domainDeck);

        entityManager.flush();
        entityManager.clear(); // Force Hibernate to hit the DB for the next query

        // 3. Assert: Retrieve the deck by owner and verify data integrity
        List<Deck> retrievedDecks = deckRepositoryAdapter.findAllByOwnerId(new UserId(userId));

        assertAll("Database validation for Deck persistence",
                () -> assertFalse(retrievedDecks.isEmpty(), "Deck list should not be empty"),
                () -> assertEquals("Integration Test Deck", retrievedDecks.get(0).title().value()),
                () -> assertEquals(userId, retrievedDecks.get(0).ownerId().value()),
                () -> assertNotNull(retrievedDecks.get(0).createdAt(), "Creation timestamp must be persisted")
        );
    }
}
