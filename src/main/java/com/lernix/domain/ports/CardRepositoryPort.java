package com.lernix.domain.ports;

import com.lernix.domain.model.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CardRepositoryPort {
    Card save(Card card);
    Optional<Card> findById(CardId id);
    List<Card> findAllByDeckId(DeckId deckId);
    void deleteById(CardId id);
    boolean existsById(CardId id);
    long countByOwnerId(UserId userId);
    List<Card> findAllByTag(UserId userId, Tag tag);
    List<Card> findDueByTag(UserId userId, Tag tag, Instant now);
}

