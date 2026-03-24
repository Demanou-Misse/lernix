package com.lernix.infrastructure.persistence.repository;

import com.lernix.infrastructure.persistence.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for the Tag Dictionary.
 * 2026 Senior Standard: Ensures tag uniqueness and fast lookup by name.
 */
@Repository
public interface JpaTagRepository extends JpaRepository<TagEntity, UUID> {

    /**
     * Used by the Mapper to check if a tag already exists
     * before creating a new one (Normalization).
     */
    Optional<TagEntity> findByName(String name);
}

