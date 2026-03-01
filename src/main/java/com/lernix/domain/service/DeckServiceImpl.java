package com.lernix.domain.service;

import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.DeckRepositoryPort;

public class DeckServiceImpl implements DeckService {

    private final DeckRepositoryPort deckRepositoryPort;

    public DeckServiceImpl(DeckRepositoryPort deckRepositoryPort) {
        this.deckRepositoryPort = deckRepositoryPort;
    }

    @Override
    public boolean isTitleUniqueForUser(UserId ownerId, DeckTitle title) {
        return deckRepositoryPort.findAllByOwnerId(ownerId).stream()
                .noneMatch(deck -> deck.title().value().equalsIgnoreCase(title.value().trim()));
    }
}


