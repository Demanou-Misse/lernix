package com.lernix.application.usecase.user;

import com.lernix.domain.model.Email;
import com.lernix.domain.model.PasswordHash;
import com.lernix.domain.model.User;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.shared.exception.EntityAlreadyExistsException;

public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordHasher passwordHasher;

    // Manual constructor for dependency injection
    public CreateUserUseCase(UserRepositoryPort userRepositoryPort, PasswordHasher passwordHasher) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordHasher = passwordHasher;
    }

    public User execute(String email, String plainPassword) {
        Email userEmail = new Email(email);

        if (userRepositoryPort.existsByEmail(userEmail)) {
            throw new EntityAlreadyExistsException("User with email " + email + " already exists.");
        }

        PasswordHash hash = passwordHasher.encode(plainPassword);
        User newUser = User.register(userEmail, hash);

        return userRepositoryPort.save(newUser);
    }
}



