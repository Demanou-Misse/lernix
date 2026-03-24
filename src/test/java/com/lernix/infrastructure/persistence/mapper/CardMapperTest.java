package com.lernix.infrastructure.persistence.mapper;

import com.lernix.domain.enums.CardState;
import com.lernix.domain.model.Card;
import com.lernix.infrastructure.persistence.entity.CardEntity;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.TagEntity;
import com.lernix.infrastructure.persistence.repository.JpaTagRepository;
import com.lernix.infrastructure.web.mapper.CardMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Infrastructure: Card Mapper High-Fidelity Tests")
class CardMapperTest {

    private CardMapper mapper;

    @Mock private EntityManager entityManager;
    @Mock private JpaTagRepository tagRepository;

    @BeforeEach
    void setUp() {
        // Injection manuelle pour le test unitaire
        mapper = new CardMapper(entityManager, tagRepository);
    }

    @Test
    @DisplayName("Should reconstruct Domain Card from JPA Entity correctly")
    void shouldMapEntityToDomain() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        DeckEntity deckEntity = DeckEntity.builder()
                .id(UUID.randomUUID())
                .title("Medicine")
                .build();

        // We create TagEntities because CardEntity now uses a Dictionary
        TagEntity tag1 = TagEntity.builder().id(UUID.randomUUID()).name("med").build();
        TagEntity tag2 = TagEntity.builder().id(UUID.randomUUID()).name("bio").build();

        CardEntity entity = CardEntity.builder()
                .id(cardId)
                .front("Front content")
                .back("Back content")
                .state(CardState.REVIEW)
                .easeFactor(2.8)
                .reviewInterval(10)
                .repetitions(5)
                .nextReviewDate(Instant.now())
                .tags(Set.of(tag1, tag2))
                .deck(deckEntity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // Act
        Card domain = mapper.toDomain(entity);

        // Assert
        assertThat(domain.getId().value()).isEqualTo(cardId);
        assertThat(domain.getState()).isEqualTo(CardState.REVIEW);
        assertThat(domain.getReviewMetaData().easeFactor()).isEqualTo(2.8);
        assertThat(domain.getTags()).hasSize(2);
        assertThat(domain.getTags()).extracting("value").containsExactlyInAnyOrder("med", "bio");
    }
}


