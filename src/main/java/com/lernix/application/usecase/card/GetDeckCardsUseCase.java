package com.lernix.application.usecase.card;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.shared.exception.DeckNotFoundException;

import java.util.List;

/**
 * Use Case to retrieve all cards belonging to a specific deck.
 */
public class GetDeckCardsUseCase {

    private final CardRepositoryPort cardRepositoryPort;
    private final DeckRepositoryPort deckRepositoryPort;

    public GetDeckCardsUseCase(CardRepositoryPort cardRepositoryPort, DeckRepositoryPort deckRepositoryPort) {
        this.cardRepositoryPort = cardRepositoryPort;
        this.deckRepositoryPort = deckRepositoryPort;
    }

    public List<Card> execute(DeckId deckId) {
        // 1. Business Validation: The deck must exist to list its cards
        if (!deckRepositoryPort.existsById(deckId)) {
            throw new DeckNotFoundException("Cannot list cards: Deck not found with ID " + deckId.value());
        }

        // 2. Fetch inventory
        return cardRepositoryPort.findAllByDeckId(deckId);
    }
}

