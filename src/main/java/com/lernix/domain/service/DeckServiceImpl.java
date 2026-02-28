package com.lernix.domain.service;

import com.lernix.domain.model.DeckTitle;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.DeckRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepositoryPort deckRepositoryPort;

    @Override
    public boolean isTitleUniqueForUser(UserId ownerId, DeckTitle title) {
        return deckRepositoryPort.findAllByOwnerId(ownerId).stream()
                .noneMatch(existingDeck ->
                        existingDeck.title().value().equalsIgnoreCase(title.value().trim())
                );
    }
}

