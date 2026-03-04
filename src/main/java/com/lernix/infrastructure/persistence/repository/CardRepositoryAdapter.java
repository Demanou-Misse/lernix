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

@Component
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepositoryPort {

    private final JpaCardRepository jpaRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Card save(Card card) {
        CardEntity entity = toEntity(card);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findById(CardId id) {
        return jpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAllByDeckId(DeckId deckId) {
        return jpaRepository.findAllByDeckId(deckId.value())
                .stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(CardId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void deleteById(CardId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDeckId(DeckId deckId) {
        return jpaRepository.countByDeckId(deckId.value());
    }

    // --- High Performance Mappers ---

    private CardEntity toEntity(Card domain) {
        // Optimization: Use Proxy for Deck to avoid unnecessary SELECT
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

