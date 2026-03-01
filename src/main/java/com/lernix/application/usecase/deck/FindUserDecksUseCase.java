package com.lernix.application.usecase.deck;

import com.lernix.domain.model.Deck;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.DeckRepositoryPort;
import java.util.List;

public class FindUserDecksUseCase {

    private final DeckRepositoryPort deckRepositoryPort;

    public FindUserDecksUseCase(DeckRepositoryPort deckRepositoryPort) {
        this.deckRepositoryPort = deckRepositoryPort;
    }

    public List<Deck> execute(UserId ownerId) {
        return deckRepositoryPort.findAllByOwnerId(ownerId);
    }
}

