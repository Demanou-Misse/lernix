package com.lernix.domain.ports;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.UserId;

import java.util.List;
import java.util.Optional;

public interface CardRepositoryPort {
    Card save(Card card);
    Optional<Card> findById(CardId id);
    List<Card> findAllByDeckId(DeckId deckId);
    void deleteById(CardId id);
    boolean existsById(CardId id);
    long countByOwnerId(UserId userId);
}

