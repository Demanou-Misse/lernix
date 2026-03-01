package com.lernix.application.usecase.deck;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.shared.exception.DeckNotFoundException;

public class ArchiveDeckUseCase {

    private final DeckRepositoryPort deckRepositoryPort;

    public ArchiveDeckUseCase(DeckRepositoryPort deckRepositoryPort) {
        this.deckRepositoryPort = deckRepositoryPort;
    }

    public Deck execute(DeckId deckId) {
        Deck existingDeck = deckRepositoryPort.findById(deckId)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found: " + deckId.value()));

        // Business rule: delegate state transition to the Domain model
        Deck archivedDeck = existingDeck.archive();

        return deckRepositoryPort.save(archivedDeck);
    }
}

