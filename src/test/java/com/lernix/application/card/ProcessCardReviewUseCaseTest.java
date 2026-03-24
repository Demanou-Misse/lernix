package com.lernix.application.card;

import com.lernix.application.usecase.card.ProcessCardReviewUseCase;
import com.lernix.domain.algorithm.SpacedRepetitionEngine;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Process Card Review Use Case")
class ProcessCardReviewUseCaseTest {

    @Mock private CardRepositoryPort cardRepository;
    @Mock private SpacedRepetitionEngine repetitionEngine;

    @InjectMocks private ProcessCardReviewUseCase useCase;

    @Test
    @DisplayName("Should successfully orchestrate a full review cycle")
    void shouldExecuteFullReviewCycle() {
        // Arrange
        CardId cardId = new CardId(UUID.randomUUID());
        Card card = mock(Card.class); // Mocking the Aggregate
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        useCase.execute(cardId, ReviewGrade.GOOD);

        // Assert
        verify(cardRepository).findById(cardId);
        verify(card).applyReview(ReviewGrade.GOOD, repetitionEngine);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card does not exist")
    void shouldFailWhenCardMissing() {
        CardId cardId = new CardId(UUID.randomUUID());
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(cardId, ReviewGrade.EASY))
                .isInstanceOf(CardNotFoundException.class);

        verify(cardRepository, never()).save(any());
    }
}
