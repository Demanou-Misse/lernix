package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.model.*;
import com.lernix.infrastructure.persistence.entity.CardEntity;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.TagEntity;
import com.lernix.infrastructure.persistence.repository.JpaTagRepository;
import com.lernix.infrastructure.web.dto.response.CardResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Enterprise Grade Mapper: Bridges Web DTOs, Rich Domain Models, and JPA Entities.
 * 2026 Standard: Pure manual mapping for full control over Value Objects and dictionary normalization.
 */
@Component
@RequiredArgsConstructor
public class CardMapper {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JpaTagRepository tagRepository;

    // --- DOMAIN TO WEB (API Response) ---

    public CardResponse toResponse(Card domain) {
        if (domain == null) return null;

        return new CardResponse(
                domain.getId().value(),
                domain.getDeckId().value(),
                domain.getContent().front(),
                domain.getContent().back(),
                domain.getState().name(),
                domain.getReviewMetaData().nextReview(),
                domain.getTags().stream().map(Tag::value).collect(Collectors.toSet()),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public List<CardResponse> toResponseList(List<Card> domains) {
        return domains.stream()
                .map(this::toResponse)
                .toList();
    }

    // --- JPA TO DOMAIN (Aggregate Reconstruction) ---

    public Card toDomain(CardEntity entity) {
        if (entity == null) return null;

        return new Card(
                new CardId(entity.getId()),
                new DeckId(entity.getDeck().getId()),
                new CardContent(entity.getFront(), entity.getBack()),
                entity.getState(),
                new ReviewMetaData(
                        entity.getEaseFactor(),
                        entity.getReviewInterval(),
                        entity.getRepetitions(),
                        entity.getNextReviewDate()
                ),
                entity.getTags().stream()
                        .map(tagEntity -> new Tag(tagEntity.getName()))
                        .collect(Collectors.toSet()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // --- DOMAIN TO JPA (Persistence) ---

    public CardEntity toEntity(Card domain) {
        if (domain == null) return null;

        // Optimization: Use proxy for Deck to avoid extra SELECT
        DeckEntity deckProxy = entityManager.getReference(DeckEntity.class, domain.getDeckId().value());

        return CardEntity.builder()
                .id(domain.getId().value())
                .front(domain.getContent().front())
                .back(domain.getContent().back())
                .state(domain.getState())
                .easeFactor(domain.getReviewMetaData().easeFactor())
                .reviewInterval(domain.getReviewMetaData().interval())
                .repetitions(domain.getReviewMetaData().repetitions())
                .nextReviewDate(domain.getReviewMetaData().nextReview())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .deck(deckProxy)
                .tags(domain.getTags().stream()
                        .map(tag -> tagRepository.findByName(tag.value())
                                .orElseGet(() -> TagEntity.builder()
                                        .id(UUID.randomUUID())
                                        .name(tag.value())
                                        .build()))
                        .collect(Collectors.toSet()))
                .build();
    }
}



