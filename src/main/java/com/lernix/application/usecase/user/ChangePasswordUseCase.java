package com.lernix.application.usecase.user;

import com.lernix.domain.model.PasswordHash;
import com.lernix.domain.model.User;
import com.lernix.domain.model.UserId;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.InvalidPasswordException;
import com.lernix.shared.exception.UserNotFoundException;

/**
 * Handles secure password rotation.
 * Enforces business security rules before persisting the new state.
 */
public class ChangePasswordUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordHasher passwordHasher;

    public ChangePasswordUseCase(UserRepositoryPort userRepositoryPort, PasswordHasher passwordHasher) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordHasher = passwordHasher;
    }

    public void execute(UserId userId, String currentPlainPassword, String newPlainPassword) {
        // 1. Identification
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Account not found"));

        // 2. Identity Verification (Security Gate)
        // matches() prend un PasswordHash du domaine, pas une String
        if (!passwordHasher.matches(currentPlainPassword, user.passwordHash())) {
            throw new InvalidPasswordException("Current password verification failed. Access denied.");
        }

        // 3. Immutability: Produce new state via the aggregate
        PasswordHash newHash = passwordHasher.encode(newPlainPassword);
        User updatedUser = user.changePassword(newHash);

        // 4. Persistence
        userRepositoryPort.save(updatedUser);
    }
}

