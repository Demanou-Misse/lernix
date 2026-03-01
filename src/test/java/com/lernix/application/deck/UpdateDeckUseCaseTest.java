package com.lernix.application.deck;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import com.lernix.application.usecase.deck.UpdateDeckUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDeckUseCaseTest {

    @Mock private DeckRepositoryPort deckRepositoryPort;
    @Mock private DeckService deckService;

    @InjectMocks private UpdateDeckUseCase updateDeckUseCase;

    @Test
    @DisplayName("Should update deck when new title is unique")
    void shouldUpdateSuccessfully() {
        DeckId deckId = new DeckId(UUID.randomUUID());
        UserId ownerId = new UserId(UUID.randomUUID());
        Deck existingDeck = Deck.create(ownerId, new DeckTitle("Old Title"), "Old Desc");

        when(deckRepositoryPort.findById(deckId)).thenReturn(Optional.of(existingDeck));
        when(deckService.isTitleUniqueForUser(eq(ownerId), any(DeckTitle.class))).thenReturn(true);
        when(deckRepositoryPort.save(any(Deck.class))).thenAnswer(i -> i.getArguments()[0]);

        Deck result = updateDeckUseCase.execute(deckId, "New Title", "New Description");

        assertEquals("New Title", result.title().value());
        assertEquals("New Description", result.description());
    }

    @Test
    @DisplayName("Should throw exception when updating to a title already owned by the same user")
    void shouldFailWhenNewTitleTaken() {
        DeckId deckId = new DeckId(UUID.randomUUID());
        UserId ownerId = new UserId(UUID.randomUUID());
        Deck existingDeck = Deck.create(ownerId, new DeckTitle("My Deck"), "Desc");

        when(deckRepositoryPort.findById(deckId)).thenReturn(Optional.of(existingDeck));
        // Simulate that the new title is already used by another deck of the same user
        when(deckService.isTitleUniqueForUser(eq(ownerId), any(DeckTitle.class))).thenReturn(false);

        assertThrows(EntityAlreadyExistsException.class,
                () -> updateDeckUseCase.execute(deckId, "Taken Title", "New Desc"));
    }
}

