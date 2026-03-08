package com.lernix.application.usecase.user;

import com.lernix.domain.model.User;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.UserNotFoundException;

/**
 * Handles account suspension (Soft Delete).
 * Compliant with security policies where data is preserved for a grace period.
 */
public class DisableUserAccountUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public DisableUserAccountUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public void execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required for suspension");
        }

        // 1. Fetch
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Account not found: " + userId.value()));

        // 2. Domain Transition: The aggregate produces a new state with updated timestamp
        User disabledUser = user.disable();

        // 3. Persistence
        userRepositoryPort.save(disabledUser);
    }
}

