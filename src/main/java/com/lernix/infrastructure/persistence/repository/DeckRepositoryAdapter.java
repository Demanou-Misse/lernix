package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import com.lernix.infrastructure.web.mapper.DeckMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Persistence Adapter for Deck Aggregate.
 * Optimized for PostgreSQL 17 and high-concurrency environments.
 */
@Component
@RequiredArgsConstructor
public class DeckRepositoryAdapter implements DeckRepositoryPort {

    private final DeckMapper deckMapper;
    private final JpaDeckRepository jpaDeckRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Deck save(Deck deck) {
        DeckEntity entity =  deckMapper.toEntity(deck);
        DeckEntity savedEntity = jpaDeckRepository.save(entity);
        return deckMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findById(DeckId id) {
        return jpaDeckRepository.findById(id.value())
                .map(deckMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAllByOwnerId(UserId ownerId) {
        return jpaDeckRepository.findAllByOwnerId(ownerId.value())
                .stream()
                .map(deckMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(DeckId id) {
        return jpaDeckRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void deleteById(DeckId id) {
        // SQL Cascade handles the linked cards automatically
        jpaDeckRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByOwnerId(UserId userId) {
        // High-performance SQL COUNT via JpaRepository
        return jpaDeckRepository.countByOwnerId(userId.value());
    }

}


