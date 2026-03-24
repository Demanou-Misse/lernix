package com.lernix.infrastructure.persistence.repository;

import com.lernix.infrastructure.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCardRepository extends JpaRepository<CardEntity, UUID> {

    List<CardEntity> findAllByDeckId(UUID deckId);

    long countByDeckOwnerId(UUID ownerId);

    // Query 1: Find all cards by tag for a specific user
    @Query("SELECT c FROM CardEntity c JOIN c.tags t WHERE t.name = :tag AND c.deck.owner.id = :userId")
    List<CardEntity> findAllByTagAndOwner(@Param("tag") String tag, @Param("userId") UUID userId);

    // Query 2: Find cards DUE for review by tag for a specific user
    @Query("SELECT c FROM CardEntity c JOIN c.tags t " +
            "WHERE t.name = :tag AND c.deck.owner.id = :userId AND c.nextReviewDate <= :now")
    List<CardEntity> findDueByTagAndOwner(@Param("tag") String tag,
                                          @Param("userId") UUID userId,
                                          @Param("now") Instant now);
}


