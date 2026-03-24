package com.lernix.usecase.user;

import com.lernix.application.usecase.user.DisableUserAccountUseCase;
import com.lernix.domain.enums.UserStatus;
import com.lernix.domain.model.*;
import com.lernix.domain.ports.UserRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: DisableUserAccountUseCase Unit Tests")
class DisableUserAccountUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @InjectMocks private DisableUserAccountUseCase disableUserAccountUseCase;

    @Test
    @DisplayName("Should transition user to DISABLED status")
    void shouldDisableAccount() {
        // 1. Given
        UserId userId = UserId.generate();
        PasswordHash validHash = new PasswordHash("a".repeat(60));
        User user = User.register(new Email("test@test.com"), validHash);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));

        // 2. When
        disableUserAccountUseCase.execute(userId);

        // 3. Then
        verify(userRepositoryPort).save(argThat(u -> u.status() == UserStatus.DISABLED));
    }

}

