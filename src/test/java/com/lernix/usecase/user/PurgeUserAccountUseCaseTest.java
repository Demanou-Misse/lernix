package com.lernix.usecase.user;

import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: PurgeUserAccountUseCase Unit Tests")
class PurgeUserAccountUseCaseTest {

    @Mock private UserRepositoryPort userRepositoryPort;
    @InjectMocks private com.lernix.usecase.user.PurgeUserAccountUseCase purgeUserAccountUseCase;

    @Test
    @DisplayName("Should call deleteById when user exists")
    void shouldPurgeAccount() {
        UserId userId = UserId.generate();
        when(userRepositoryPort.existsById(userId)).thenReturn(true);

        purgeUserAccountUseCase.execute(userId);

        verify(userRepositoryPort, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to purge non-existent user")
    void shouldFailWhenUserMissing() {
        UserId userId = UserId.generate();
        when(userRepositoryPort.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> purgeUserAccountUseCase.execute(userId));
        verify(userRepositoryPort, never()).deleteById(any());
    }
}

