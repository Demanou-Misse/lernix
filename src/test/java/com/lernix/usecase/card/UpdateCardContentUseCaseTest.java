package com.lernix.usecase.card;

import com.lernix.application.usecase.card.UpdateCardContentUseCase;
import com.lernix.domain.model.*;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.shared.exception.CardNotFoundException;
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
@DisplayName("Application: UpdateCardContentUseCase Unit Tests")
class UpdateCardContentUseCaseTest {

    @Mock private CardRepositoryPort cardRepositoryPort;

    @InjectMocks private UpdateCardContentUseCase updateCardContentUseCase;

    @Test
    @DisplayName("Should update card content correctly")
    void shouldUpdateCard() {
        CardId cardId = CardId.generate();
        Card existingCard = Card.create(new DeckId(UUID.randomUUID()), "Old Front", "Old Back");

        when(cardRepositoryPort.findById(cardId)).thenReturn(Optional.of(existingCard));
        when(cardRepositoryPort.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));

        Card result = updateCardContentUseCase.execute(cardId, "New Front", "New Back");

        assertAll("Verify updated card",
                () -> assertEquals("New Front", result.content().front()),
                () -> assertEquals("New Back", result.content().back()),
                () -> assertTrue(result.updatedAt().isAfter(existingCard.updatedAt())
                        || result.updatedAt().equals(existingCard.updatedAt()))
        );
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card is missing")
    void shouldFailWhenCardNotFound() {
        CardId unknownId = CardId.generate();
        when(cardRepositoryPort.findById(unknownId)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> updateCardContentUseCase.execute(unknownId, "F", "B"));
    }
}

