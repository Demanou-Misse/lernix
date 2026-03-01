package com.lernix.application.deck;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import com.lernix.shared.exception.UserNotFoundException;
import com.lernix.application.usecase.deck.CreateDeckUseCase;
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
class CreateDeckUseCaseTest {

    @Mock private DeckRepositoryPort deckRepositoryPort;
    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private DeckService deckService;

    @InjectMocks private CreateDeckUseCase createDeckUseCase;

    @Test
    @DisplayName("Should successfully create a deck when owner exists and title is unique")
    void shouldCreateDeckSuccessfully() {
        UserId ownerId = new UserId(UUID.randomUUID());
        String title = "Organic Chemistry";

        when(userRepositoryPort.existsById(ownerId)).thenReturn(true);
        when(deckService.isTitleUniqueForUser(eq(ownerId), any(DeckTitle.class))).thenReturn(true);
        when(deckRepositoryPort.save(any(Deck.class))).thenAnswer(i -> i.getArguments()[0]);

        Deck result = createDeckUseCase.execute(ownerId, title, "Medical school notes");

        assertNotNull(result);
        assertEquals(title, result.title().value());
        verify(deckRepositoryPort, times(1)).save(any(Deck.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when owner does not exist")
    void shouldThrowExceptionWhenUserNotFound() {
        UserId unknownId = new UserId(UUID.randomUUID());
        when(userRepositoryPort.existsById(unknownId)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> createDeckUseCase.execute(unknownId, "Title", "Desc"));

        verify(deckRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw EntityAlreadyExistsException when title is already taken by the user")
    void shouldThrowExceptionWhenTitleTaken() {
        UserId ownerId = new UserId(UUID.randomUUID());
        when(userRepositoryPort.existsById(ownerId)).thenReturn(true);
        when(deckService.isTitleUniqueForUser(eq(ownerId), any(DeckTitle.class))).thenReturn(false);

        assertThrows(EntityAlreadyExistsException.class,
                () -> createDeckUseCase.execute(ownerId, "Duplicate Title", "Desc"));
    }
}

