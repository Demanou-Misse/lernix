package com.lernix.usecase.card;

import com.lernix.application.usecase.card.CreateCardUseCase;
import com.lernix.domain.model.*;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.shared.exception.DeckNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: CreateCardUseCase Unit Tests")
class CreateCardUseCaseTest {

    @Mock private CardRepositoryPort cardRepositoryPort;
    @Mock private DeckRepositoryPort deckRepositoryPort;

    @InjectMocks private CreateCardUseCase createCardUseCase;

    @Test
    @DisplayName("Should successfully create a card when deck exists")
    void shouldCreateCardSuccessfully() {
        DeckId deckId = new DeckId(UUID.randomUUID());

        when(deckRepositoryPort.existsById(deckId)).thenReturn(true);
        when(cardRepositoryPort.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Card result = createCardUseCase.execute(deckId, "Question", "Answer");

        assertAll("Verify created card",
                () -> assertNotNull(result),
                () -> assertEquals("Question", result.content().front()),
                () -> assertEquals(deckId, result.deckId())
        );
        verify(cardRepositoryPort, times(1)).save(any(Card.class));
    }

    @Test
    @DisplayName("Should throw DeckNotFoundException when deck does not exist")
    void shouldFailWhenDeckNotFound() {
        DeckId unknownDeckId = new DeckId(UUID.randomUUID());
        when(deckRepositoryPort.existsById(unknownDeckId)).thenReturn(false);

        assertThrows(DeckNotFoundException.class,
                () -> createCardUseCase.execute(unknownDeckId, "Front", "Back"));

        verify(cardRepositoryPort, never()).save(any());
    }
}

