package com.lernix.application.usecase.deck;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import com.lernix.shared.exception.UserNotFoundException;

public class CreateDeckUseCase {

    private final DeckRepositoryPort deckRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final DeckService deckService;

    // Manual constructor for pure dependency injection
    public CreateDeckUseCase(DeckRepositoryPort deckRepositoryPort,
                             UserRepositoryPort userRepositoryPort,
                             DeckService deckService) {
        this.deckRepositoryPort = deckRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.deckService = deckService;
    }

    public Deck execute(UserId ownerId, String title, String description) {
        if (!userRepositoryPort.existsById(ownerId)) {
            throw new UserNotFoundException("User not found: " + ownerId.value());
        }

        DeckTitle deckTitle = new DeckTitle(title);
        if (!deckService.isTitleUniqueForUser(ownerId, deckTitle)) {
            throw new EntityAlreadyExistsException("Deck title already exists for this user.");
        }

        Deck newDeck = Deck.create(ownerId, deckTitle, description);
        return deckRepositoryPort.save(newDeck);
    }
}


