package com.lernix.infrastructure.persistence.repository;

import com.lernix.infrastructure.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCardRepository extends JpaRepository<CardEntity, UUID> {

    List<CardEntity> findAllByDeckId(UUID deckId);

    long countByDeckId(UUID deckId);
}

