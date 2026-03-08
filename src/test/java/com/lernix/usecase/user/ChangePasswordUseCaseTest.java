package com.lernix.usecase.user;

import com.lernix.application.usecase.user.ChangePasswordUseCase;
import com.lernix.domain.model.*;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.InvalidPasswordException;
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
@DisplayName("Application: ChangePasswordUseCase Unit Tests")
class ChangePasswordUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @Mock private PasswordHasher passwordHasher;
    @InjectMocks private ChangePasswordUseCase changePasswordUseCase;

    @Test
    @DisplayName("Should update password when current password is valid")
    void shouldUpdatePassword() {
        UserId userId = UserId.generate();
        PasswordHash validOldHash = new PasswordHash("a".repeat(60));
        User user = User.register(new Email("test@test.com"), validOldHash);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));
        when(passwordHasher.matches("correct_pass", user.passwordHash())).thenReturn(true);
        when(passwordHasher.encode("new_pass")).thenReturn(new PasswordHash("b".repeat(60)));

        assertDoesNotThrow(() -> changePasswordUseCase.execute(userId, "correct_pass", "new_pass"));
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when current password is wrong")
    void shouldFailOnWrongCurrentPassword() {
        UserId userId = UserId.generate();
        PasswordHash validHash = new PasswordHash("a".repeat(60));
        User user = User.register(new Email("test@test.com"), validHash);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));
        when(passwordHasher.matches("wrong_pass", user.passwordHash())).thenReturn(false);

        assertThrows(InvalidPasswordException.class,
                () -> changePasswordUseCase.execute(userId, "wrong_pass", "new_pass"));

        verify(userRepositoryPort, never()).save(any());
    }

}

