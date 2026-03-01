package com.lernix.application.usecase.deck;

import com.lernix.domain.model.DeckId;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.shared.exception.DeckNotFoundException;

public class DeleteDeckUseCase {

    private final DeckRepositoryPort deckRepositoryPort;

    public DeleteDeckUseCase(DeckRepositoryPort deckRepositoryPort) {
        this.deckRepositoryPort = deckRepositoryPort;
    }

    public void execute(DeckId deckId) {
        if (!deckRepositoryPort.existsById(deckId)) {
            throw new DeckNotFoundException("Deck not found with ID: " + deckId.value());
        }
        deckRepositoryPort.deleteById(deckId);
    }
}

