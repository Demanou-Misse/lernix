package com.lernix.application.usecase.card;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.shared.exception.DeckNotFoundException;

/**
 * Use Case to add a new card to a specific deck.
 * Pure Java implementation.
 */
public class CreateCardUseCase {

    private final CardRepositoryPort cardRepositoryPort;
    private final DeckRepositoryPort deckRepositoryPort;

    public CreateCardUseCase(CardRepositoryPort cardRepositoryPort, DeckRepositoryPort deckRepositoryPort) {
        this.cardRepositoryPort = cardRepositoryPort;
        this.deckRepositoryPort = deckRepositoryPort;
    }

    public Card execute(DeckId deckId, String front, String back) {
        // 1. Referential Integrity check: Does the deck exist?
        if (!deckRepositoryPort.existsById(deckId)) {
            throw new DeckNotFoundException("Cannot add card: Deck not found with ID " + deckId.value());
        }

        // 2. Create the aggregate (Domain logic handles content validation)
        Card newCard = Card.create(deckId, front, back);

        // 3. Persist
        return cardRepositoryPort.save(newCard);
    }
}

