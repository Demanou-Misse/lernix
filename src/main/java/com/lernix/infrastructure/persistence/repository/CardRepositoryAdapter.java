package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.infrastructure.persistence.entity.CardEntity;
import com.lernix.infrastructure.web.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Persistence Adapter for Card Aggregate.
 * 2026 Senior Standard: High-performance orchestration between Domain and DB.
 * Delegates mapping to CardMapper and handles transactional boundaries.
 */
@Component
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepositoryPort {

    private final JpaCardRepository jpaCardRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public Card save(Card card) {
        // Delegate complex mapping (including Tag dictionary lookup) to the mapper
        CardEntity entity = cardMapper.toEntity(card);
        CardEntity savedEntity = jpaCardRepository.save(entity);
        return cardMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findById(CardId id) {
        return jpaCardRepository.findById(id.value())
                .map(cardMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAllByTag(UserId userId, Tag tag) {
        return jpaCardRepository.findAllByTagAndOwner(tag.value(), userId.value())
                .stream()
                .map(cardMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findDueByTag(UserId userId, Tag tag, Instant now) {
        return jpaCardRepository.findDueByTagAndOwner(tag.value(), userId.value(), now)
                .stream()
                .map(cardMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAllByDeckId(DeckId deckId) {
        return jpaCardRepository.findAllByDeckId(deckId.value())
                .stream()
                .map(cardMapper::toDomain)
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

    @Override
    @Transactional(readOnly = true)
    public long countByOwnerId(UserId userId) {
        return jpaCardRepository.countByDeckOwnerId(userId.value());
    }
}




