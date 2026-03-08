package com.lernix.usecase.user;

import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.UserNotFoundException;

/**
 * Permanently erases a user account and all associated resources.
 * Following GDPR compliance standards.
 */
public class PurgeUserAccountUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public PurgeUserAccountUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public void execute(UserId userId) {
        // 1. Existence check to prevent silent failures
        if (!userRepositoryPort.existsById(userId)) {
            throw new UserNotFoundException("Purge failed: Account " + userId.value() + " does not exist.");
        }

        // 2. Physical Deletion: Infrastructure (SQL) handles the cascading of Decks and Cards
        userRepositoryPort.deleteById(userId);
    }
}

