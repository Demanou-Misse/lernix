package com.lernix.application.card;

import com.lernix.application.usecase.card.GetCardsByTagUseCase;
import com.lernix.domain.model.Card;
import com.lernix.domain.model.Tag;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.CardRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Get Cards By Tag Use Case")
class GetCardsByTagUseCaseTest {

    @Mock private CardRepositoryPort cardRepository;

    @InjectMocks private GetCardsByTagUseCase useCase;

    @Test
    @DisplayName("Should call findDueByTag when onlyDue is true")
    void shouldFetchOnlyDueCards() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        String tagName = "medicine";
        when(cardRepository.findDueByTag(eq(userId), any(Tag.class), any()))
                .thenReturn(List.of(mock(Card.class)));

        // Act
        List<Card> results = useCase.execute(userId, tagName, true);

        // Assert
        assertThat(results).hasSize(1);
        verify(cardRepository).findDueByTag(eq(userId), eq(new Tag("medicine")), any());
        verify(cardRepository, never()).findAllByTag(any(), any());
    }

    @Test
    @DisplayName("Should fail immediately if tag name is invalid format")
    void shouldFailOnInvalidTag() {
        UserId userId = new UserId(UUID.randomUUID());

        // Note: The UseCase will crash at 'new Tag("a")' before reaching the repo
        assertThatThrownBy(() -> useCase.execute(userId, "a", false))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(cardRepository);
    }
}

