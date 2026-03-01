package com.lernix.domain.service;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.DeckRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

    @Mock
    private DeckRepositoryPort deckRepositoryPort;

    @InjectMocks
    private DeckServiceImpl deckService;

    @Test
    @DisplayName("Should return false when title already exists for the same user (case insensitive)")
    void shouldReturnFalseWhenTitleExists() {
        UserId ownerId = new UserId(UUID.randomUUID());
        DeckTitle title = new DeckTitle("Physics");
        Deck existingDeck = Deck.create(ownerId, new DeckTitle("PHYSICS"), "");

        when(deckRepositoryPort.findAllByOwnerId(ownerId)).thenReturn(List.of(existingDeck));

        boolean isUnique = deckService.isTitleUniqueForUser(ownerId, title);

        assertFalse(isUnique);
    }

    @Test
    @DisplayName("Should return true when title is new for this user")
    void shouldReturnTrueWhenTitleIsUnique() {
        UserId ownerId = new UserId(UUID.randomUUID());
        DeckTitle title = new DeckTitle("History");

        when(deckRepositoryPort.findAllByOwnerId(ownerId)).thenReturn(List.of());

        boolean isUnique = deckService.isTitleUniqueForUser(ownerId, title);

        assertTrue(isUnique);
    }
}

