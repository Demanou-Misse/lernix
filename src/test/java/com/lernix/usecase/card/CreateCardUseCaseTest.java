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

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
        Set<String> tagNames = Set.of("med", "urgent");

        when(deckRepositoryPort.existsById(deckId)).thenReturn(true);
        when(cardRepositoryPort.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Card result = createCardUseCase.execute(deckId, "Question", "Answer", tagNames);

        assertAll("Verify created card",
                () -> assertNotNull(result),
                () -> assertEquals("Question", result.getContent().front()),
                () -> assertThat(result.getTags()).hasSize(2)
        );
        verify(cardRepositoryPort, times(1)).save(any(Card.class));
    }



    @Test
    @DisplayName("Should throw DeckNotFoundException when deck does not exist")
    void shouldFailWhenDeckNotFound() {
        // Arrange
        DeckId unknownDeckId = new DeckId(UUID.randomUUID());
        Set<String> emptyTags = Collections.emptySet(); // On prépare un set vide

        when(deckRepositoryPort.existsById(unknownDeckId)).thenReturn(false);

        // Act & Assert
        assertThrows(DeckNotFoundException.class,
                () -> createCardUseCase.execute(unknownDeckId, "Front", "Back", emptyTags)); // FIX: Suppression virgule + ajout argument

        verify(cardRepositoryPort, never()).save(any());
    }

}

