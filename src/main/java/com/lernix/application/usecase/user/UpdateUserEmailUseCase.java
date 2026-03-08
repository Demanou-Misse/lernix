package com.lernix.application.usecase.user;

import com.lernix.domain.model.Email;
import com.lernix.domain.model.User;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import com.lernix.shared.exception.UserNotFoundException;

import java.time.Instant;

/**
 * Manages user email updates with uniqueness constraints.
 */
public class UpdateUserEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserEmailUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public User execute(UserId userId, String newEmailRaw) {
        // Validation immédiate via le Value Object Email
        Email newEmail = new Email(newEmailRaw);

        // 1. Identification
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 2. Idempotency check
        if (user.email().equals(newEmail)) {
            return user;
        }

        // 3. Global Uniqueness check
        if (userRepositoryPort.existsByEmail(newEmail)) {
            throw new EntityAlreadyExistsException("Email already taken: " + newEmail.value());
        }

        // 4. Domain Transformation
        User updatedUser = new User(
                user.id(),
                newEmail,
                user.passwordHash(),
                user.status(),
                user.createdAt(),
                Instant.now(), // Refresh update audit
                user.version()
        );

        return userRepositoryPort.save(updatedUser);
    }
}
