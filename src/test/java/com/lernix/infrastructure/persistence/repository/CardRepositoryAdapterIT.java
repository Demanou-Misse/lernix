package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.domain.enums.CardState;
import com.lernix.infrastructure.persistence.entity.CardEntity;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.TagEntity;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import com.lernix.infrastructure.web.mapper.CardMapper; // Required for the Adapter
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Enterprise Integration Test for Card Persistence.
 * 2026 Senior Standard: Uses TestEntityManager to validate real SQL/JPQL joins.
 */
@DataJpaTest
@Import({CardRepositoryAdapter.class, CardMapper.class}) // Import both for proper wiring
@DisplayName("Infrastructure: Card Repository Integration Test")
class CardRepositoryAdapterIT {

    @Autowired
    private CardRepositoryAdapter adapter;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should find cards by tag and owner correctly using Normalized Dictionary")
    void shouldFindCardsByTag() {
        // 1. Arrange: Setup User (Owner)
        UserEntity owner = UserEntity.builder()
                .id(UUID.randomUUID())
                .email("test@lernix.io")
                .passwordHash("hashed-pw")
                .status("ACTIVE")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        entityManager.persist(owner);

        // 2. Arrange: Setup Deck
        DeckEntity deck = DeckEntity.builder()
                .id(UUID.randomUUID())
                .owner(owner)
                .title("Medicine")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        entityManager.persist(deck);

        // 3. Arrange: Setup Tag Dictionary (Normalization)
        TagEntity cardioTag = TagEntity.builder()
                .id(UUID.randomUUID())
                .name("cardio")
                .build();
        TagEntity anatomyTag = TagEntity.builder()
                .id(UUID.randomUUID())
                .name("anatomy")
                .build();
        entityManager.persist(cardioTag);
        entityManager.persist(anatomyTag);

        // 4. Arrange: Setup Card with SRS Stats & Tags
        CardEntity card = CardEntity.builder()
                .id(UUID.randomUUID())
                .deck(deck)
                .front("Q").back("A")
                .state(CardState.NEW)
                .easeFactor(2.5)
                .reviewInterval(0)
                .repetitions(0)
                .nextReviewDate(Instant.now())
                .tags(Set.of(cardioTag, anatomyTag)) // Using TagEntity objects
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        entityManager.persist(card);
        entityManager.flush(); // Sync with DB
        entityManager.clear(); // Clear cache to force a real SQL select in 'Act'

        // 5. Act: Search via the Adapter Port
        List<Card> results = adapter.findAllByTag(new UserId(owner.getId()), new Tag("cardio"));

        // 6. Assert: Verify Domain Mapping and Data Integrity
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTags())
                .extracting(Tag::value)
                .contains("cardio", "anatomy");

        assertThat(results.get(0).getState()).isEqualTo(CardState.NEW);
        assertThat(results.get(0).getReviewMetaData().easeFactor()).isEqualTo(2.5);
    }
}

