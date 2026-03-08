package com.lernix.usecase.user;

import com.lernix.application.usecase.user.UpdateUserEmailUseCase;
import com.lernix.domain.model.*;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: UpdateUserEmailUseCase Unit Tests")
class UpdateUserEmailUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UpdateUserEmailUseCase updateUserEmailUseCase;

    @Test
    @DisplayName("Should successfully update email when new email is unique")
    void shouldUpdateEmailSuccessfully() {
        // 1. Given
        UserId userId = UserId.generate();
        PasswordHash validHash = new PasswordHash("a".repeat(60));
        User existingUser = User.register(new Email("old@test.com"), validHash);
        String newEmailRaw = "new@test.com";

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.existsByEmail(new Email(newEmailRaw))).thenReturn(false);
        when(userRepositoryPort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // 2. When
        User result = updateUserEmailUseCase.execute(userId, newEmailRaw);

        // 3. Then
        assertAll("Email update validation",
                () -> assertEquals(newEmailRaw, result.email().value()),
                () -> assertTrue(result.updatedAt().isAfter(existingUser.updatedAt()))
        );
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should do nothing (idempotency) if new email is same as current")
    void shouldBeIdempotentWhenEmailIsSame() {
        // 1. Given
        UserId userId = UserId.generate();
        Email currentEmail = new Email("same@test.com");
        PasswordHash validHash = new PasswordHash("a".repeat(60)); // FIX
        User existingUser = User.register(currentEmail, validHash);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));

        // 2. When
        User result = updateUserEmailUseCase.execute(userId, currentEmail.value());

        // 3. Then
        assertEquals(existingUser, result);
        verify(userRepositoryPort, never()).existsByEmail(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw EntityAlreadyExistsException if new email is taken by another user")
    void shouldFailWhenEmailAlreadyExists() {
        // 1. Given
        UserId userId = UserId.generate();
        PasswordHash validHash = new PasswordHash("a".repeat(60)); // FIX
        User existingUser = User.register(new Email("me@test.com"), validHash);
        String takenEmail = "taken@test.com";

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.existsByEmail(new Email(takenEmail))).thenReturn(true);

        // 2. When & Then
        assertThrows(EntityAlreadyExistsException.class,
                () -> updateUserEmailUseCase.execute(userId, takenEmail));

        verify(userRepositoryPort, never()).save(any());
    }

}

