package com.lernix.application.usecase.user;

import com.lernix.domain.model.User;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.UserNotFoundException;

/**
 * Orchestrates the retrieval of user profile data.
 * Pure POJO - Independent of any framework.
 */
public class GetUserProfileUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserProfileUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public User execute(UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User identifier is required");
        }

        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId.value()));
    }
}


