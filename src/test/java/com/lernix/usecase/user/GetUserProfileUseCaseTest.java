package com.lernix.usecase.user;

import com.lernix.application.usecase.user.GetUserProfileUseCase;
import com.lernix.domain.model.*;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: GetUserProfileUseCase Unit Tests")
class GetUserProfileUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @InjectMocks private GetUserProfileUseCase getUserProfileUseCase;

    @Test
    @DisplayName("Should return user when ID exists")
    void shouldReturnUser() {
        // 1. Given
        UserId userId = UserId.generate();
        PasswordHash validHash = new PasswordHash("a".repeat(60));
        User mockUser = User.register(new Email("test@test.com"), validHash);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(mockUser));

        // 2. When
        User result = getUserProfileUseCase.execute(userId);

        // 3. Then
        assertEquals(mockUser.email(), result.email());
    }


    @Test
    @DisplayName("Should throw UserNotFoundException when ID is missing")
    void shouldFailWhenUserNotFound() {
        UserId userId = UserId.generate();
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> getUserProfileUseCase.execute(userId));
    }
}

