package com.lernix.application.usecase.deck;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.shared.exception.DeckNotFoundException;

public class GetDeckDetailsUseCase {

    private final DeckRepositoryPort deckRepositoryPort;

    public GetDeckDetailsUseCase(DeckRepositoryPort deckRepositoryPort) {
        this.deckRepositoryPort = deckRepositoryPort;
    }

    public Deck execute(DeckId deckId) {
        return deckRepositoryPort.findById(deckId)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found: " + deckId.value()));
    }
}

