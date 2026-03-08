package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.infrastructure.persistence.entity.CardEntity;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Persistence Adapter for Card Aggregate.
 * Optimized for high-volume inventory management and bulk operations.
 */
@Component
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepositoryPort {

    private final JpaCardRepository jpaCardRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Card save(Card card) {
        CardEntity entity = toEntity(card);
        CardEntity savedEntity = jpaCardRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findById(CardId id) {
        return jpaCardRepository.findById(id.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAllByDeckId(DeckId deckId) {
        return jpaCardRepository.findAllByDeckId(deckId.value())
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(CardId id) {
        jpaCardRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(CardId id) {
        return jpaCardRepository.existsById(id.value());
    }

    /**
     * Strategic Optimization for Issue #5 Statistics.
     * Counts cards across all decks owned by the user using a single SQL Join.
     */
    @Override
    @Transactional(readOnly = true)
    public long countByOwnerId(UserId userId) {
        return jpaCardRepository.countByDeckOwnerId(userId.value());
    }

    // --- Enterprise Grade Mappers ---

    /**
     * Maps Domain Card to Infrastructure Entity.
     * PERFORMANCE: Uses EntityManager.getReference to avoid loading the full Deck object.
     */
    private CardEntity toEntity(Card domain) {
        // Optimization: Create a Proxy for the Deck to avoid an unnecessary SELECT
        DeckEntity deckProxy = entityManager.getReference(DeckEntity.class, domain.deckId().value());

        return CardEntity.builder()
                .id(domain.id().value())
                .front(domain.content().front())
                .back(domain.content().back())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .deck(deckProxy)
                .build();
    }

    /**
     * Maps Infrastructure Entity to Domain Card (Record).
     */
    private Card toDomain(CardEntity entity) {
        return new Card(
                new CardId(entity.getId()),
                new DeckId(entity.getDeck().getId()),
                new CardContent(entity.getFront(), entity.getBack()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}


