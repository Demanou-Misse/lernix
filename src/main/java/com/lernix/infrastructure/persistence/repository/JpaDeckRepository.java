package com.lernix.infrastructure.persistence.repository;

import com.lernix.infrastructure.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface JpaDeckRepository extends JpaRepository<DeckEntity, UUID> {
    Set<DeckEntity> findAllByOwnerId(UUID ownerId);
    long countByOwnerId(UUID value);
}


