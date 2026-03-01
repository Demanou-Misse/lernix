package com.lernix.domain.ports;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.UserId;
import java.util.List;
import java.util.Optional;

public interface DeckRepositoryPort {
    Deck save(Deck deck);
    Optional<Deck> findById(DeckId id);
    List<Deck> findAllByOwnerId(UserId ownerId);
    boolean existsById(DeckId id);
    void deleteById(DeckId id);
}


