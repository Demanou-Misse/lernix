package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.infrastructure.persistence.entity.DeckEntity;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * High-performance Persistence Adapter for Deck Aggregate.
 * Decouples Domain Models from JPA Infrastructure.
 */
@Component
@RequiredArgsConstructor
public class DeckRepositoryAdapter implements DeckRepositoryPort {

    private final JpaDeckRepository jpaRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Deck save(Deck deck) {
        DeckEntity entity = toEntity(deck);
        DeckEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findById(DeckId id) {
        return jpaRepository.findById(id.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAllByOwnerId(UserId ownerId) {
        return jpaRepository.findAllByOwnerId(ownerId.value())
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(DeckId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void deleteById(DeckId id) {
        jpaRepository.deleteById(id.value());
    }

    // --- High-Performance Mappers ---

    /**
     * Maps Domain Aggregate to JPA Entity.
     * Note: Uses EntityManager.getReference to link the User without an extra SELECT query.
     */
    private DeckEntity toEntity(Deck domain) {
        // Optimization: We don't need to load the full UserEntity from DB, just a proxy with the ID
        UserEntity ownerProxy = entityManager.getReference(UserEntity.class, domain.ownerId().value());

        return DeckEntity.builder()
                .id(domain.id().value())
                .title(domain.title().value())
                .description(domain.description())
                .status(domain.status().name())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .owner(ownerProxy)
                .build();
    }

    /**
     * Maps JPA Entity back to Domain Aggregate.
     */
    private Deck toDomain(DeckEntity entity) {
        return new Deck(
                new DeckId(entity.getId()),
                new UserId(entity.getOwner().getId()), // Lazy-safe access
                new DeckTitle(entity.getTitle()),
                entity.getDescription(),
                DeckStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

