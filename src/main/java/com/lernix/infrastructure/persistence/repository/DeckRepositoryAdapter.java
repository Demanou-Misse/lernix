package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.model.UserId;
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

/**
 * Persistence Adapter for Deck Aggregate.
 * Optimized for PostgreSQL 17 and high-concurrency environments.
 */
@Component
@RequiredArgsConstructor
public class DeckRepositoryAdapter implements DeckRepositoryPort {

    private final JpaDeckRepository jpaDeckRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Deck save(Deck deck) {
        DeckEntity entity = toEntity(deck);
        DeckEntity savedEntity = jpaDeckRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findById(DeckId id) {
        return jpaDeckRepository.findById(id.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAllByOwnerId(UserId ownerId) {
        return jpaDeckRepository.findAllByOwnerId(ownerId.value())
                .stream()
                .map(this::toDomain)
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

    // --- Enterprise Grade Mappers ---

    /**
     * Maps Domain Deck to Infrastructure Entity.
     * PERFORMANCE: Uses EntityManager.getReference to avoid a database roundtrip for the Owner.
     */
    private DeckEntity toEntity(Deck domain) {
        // Use a Proxy for the UserEntity because we only need the ID for the Foreign Key
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
     * Maps Infrastructure Entity to Domain Deck (Record).
     */
    private Deck toDomain(DeckEntity entity) {
        return new Deck(
                new DeckId(entity.getId()),
                new UserId(entity.getOwner().getId()),
                new DeckTitle(entity.getTitle()),
                entity.getDescription(),
                com.lernix.domain.model.DeckStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}


