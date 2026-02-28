package com.lernix.application;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import com.lernix.application.usecase.user.CreateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordHasher passwordHasher;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCase(userRepositoryPort, passwordHasher);
    }

    @Test
    @DisplayName("Should successfully create a user when email is unique")
    void shouldCreateUserSuccessfully() {
        // Given
        String emailStr = "new@lernix.com";
        String pass = "securePassword123";
        PasswordHash mockHash = new PasswordHash("$2a$12$mockedhashvalueatleast30charslong");

        when(userRepositoryPort.existsByEmail(any(Email.class))).thenReturn(false);
        when(passwordHasher.encode(pass)).thenReturn(mockHash);
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User result = createUserUseCase.execute(emailStr, pass);

        // Then
        assertNotNull(result);
        assertEquals(emailStr, result.email().value());
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        when(userRepositoryPort.existsByEmail(any(Email.class))).thenReturn(true);

        // When & Then
        assertThrows(EntityAlreadyExistsException.class,
                () -> createUserUseCase.execute("existing@lernix.com", "password"));

        verify(userRepositoryPort, never()).save(any(User.class));
    }
}

