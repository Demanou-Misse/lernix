package com.lernix.application.usecase.deck;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckId;
import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.shared.exception.DeckNotFoundException;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import java.time.Instant;

public class UpdateDeckUseCase {

    private final DeckRepositoryPort deckRepositoryPort;
    private final DeckService deckService;

    public UpdateDeckUseCase(DeckRepositoryPort deckRepositoryPort, DeckService deckService) {
        this.deckRepositoryPort = deckRepositoryPort;
        this.deckService = deckService;
    }

    public Deck execute(DeckId deckId, String newTitle, String newDescription) {
        Deck existingDeck = deckRepositoryPort.findById(deckId)
                .orElseThrow(() -> new DeckNotFoundException("Deck not found: " + deckId.value()));

        DeckTitle updatedTitle = new DeckTitle(newTitle);
        if (!existingDeck.title().equals(updatedTitle) &&
                !deckService.isTitleUniqueForUser(existingDeck.ownerId(), updatedTitle)) {
            throw new EntityAlreadyExistsException("Title already taken by another deck.");
        }

        // Return a new Record instance (Immutability)
        Deck updatedDeck = new Deck(
                existingDeck.id(),
                existingDeck.ownerId(),
                updatedTitle,
                newDescription != null ? newDescription.trim() : existingDeck.description(),
                existingDeck.status(),
                existingDeck.createdAt(),
                Instant.now()
        );

        return deckRepositoryPort.save(updatedDeck);
    }
}

