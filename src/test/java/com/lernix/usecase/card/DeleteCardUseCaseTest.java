package com.lernix.usecase.card;

import com.lernix.application.usecase.card.DeleteCardUseCase;
import com.lernix.domain.model.CardId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.shared.exception.CardNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: DeleteCardUseCase Unit Tests")
class DeleteCardUseCaseTest {

    @Mock
    private CardRepositoryPort cardRepositoryPort;

    @InjectMocks
    private DeleteCardUseCase deleteCardUseCase;

    @Test
    @DisplayName("Should successfully delete card when it exists")
    void shouldDeleteCardSuccessfully() {
        // 1. Given
        CardId cardId = CardId.generate();
        when(cardRepositoryPort.existsById(cardId)).thenReturn(true);

        // 2. When
        assertDoesNotThrow(() -> deleteCardUseCase.execute(cardId));

        // 3. Then
        verify(cardRepositoryPort, times(1)).deleteById(cardId);
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card to delete is missing")
    void shouldFailWhenCardToDeleteNotFound() {
        // 1. Given
        CardId unknownId = CardId.generate();
        when(cardRepositoryPort.existsById(unknownId)).thenReturn(false);

        // 2. When & Then
        assertThrows(CardNotFoundException.class,
                () -> deleteCardUseCase.execute(unknownId));

        // Security check: Ensure deleteById was NEVER called
        verify(cardRepositoryPort, never()).deleteById(any());
    }
}

