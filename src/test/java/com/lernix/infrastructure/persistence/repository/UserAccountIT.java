package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.infrastructure.persistence.entity.CardEntity;
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
@Import(UserRepositoryAdapter.class)
@DisplayName("Infrastructure: User Account Integration Tests")
class UserAccountIT {

    @Autowired private UserRepositoryAdapter userRepositoryAdapter;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("Should trigger SQL Cascade Delete: Purging User must remove Decks and Cards")
    void shouldPurgeEverythingWhenUserIsDeleted() {
        // 1. Given: A complete hierarchy (User -> Deck -> Card)
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
                .id(userId).email("purge-"+userId+"@test.com").passwordHash("h")
                .status("ACTIVE").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        entityManager.persist(user);

        UUID deckId = UUID.randomUUID();
        DeckEntity deck = DeckEntity.builder()
                .id(deckId).title("Deck to Purge").status("ACTIVE").owner(user)
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();
        entityManager.persist(deck);

        CardEntity card = CardEntity.builder()
                .id(UUID.randomUUID()).front("Q").back("A").deck(deck)
                .createdAt(Instant.now()).updatedAt(Instant.now()).build();
        entityManager.persist(card);

        entityManager.flush();
        entityManager.clear();

        // 2. When: Hard deleting the User via the Adapter
        userRepositoryAdapter.deleteById(new UserId(userId));
        entityManager.flush();

        // 3. Then: Verify that all tables are cleaned up
        assertAll("Cascade Integrity Check",
                () -> assertNull(entityManager.find(UserEntity.class, userId), "User should be gone"),
                () -> assertNull(entityManager.find(DeckEntity.class, deckId), "Deck should be cascaded"),
                () -> assertTrue(entityManager.createQuery("SELECT c FROM CardEntity c WHERE c.deck.id = :id")
                        .setParameter("id", deckId).getResultList().isEmpty(), "Cards should be cascaded")
        );
    }
}

