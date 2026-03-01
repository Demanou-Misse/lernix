package com.lernix.infrastructure.config;

import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.application.usecase.deck.*;
import com.lernix.application.usecase.user.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Use Case Configuration.
 * Orchestrates the wiring of Pure Java Use Cases with Infrastructure Adapters.
 * Following the Dependency Inversion Principle (DIP).
 */
@Configuration
public class UseCaseConfig {

    // --- User Domain Use Cases ---

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordHasher passwordHasher) {
        return new CreateUserUseCase(userRepositoryPort, passwordHasher);
    }

    // --- Deck Domain Use Cases ---

    @Bean
    public CreateDeckUseCase createDeckUseCase(
            DeckRepositoryPort deckRepositoryPort,
            UserRepositoryPort userRepositoryPort,
            DeckService deckService) {
        return new CreateDeckUseCase(deckRepositoryPort, userRepositoryPort, deckService);
    }

    @Bean
    public UpdateDeckUseCase updateDeckUseCase(
            DeckRepositoryPort deckRepositoryPort,
            DeckService deckService) {
        return new UpdateDeckUseCase(deckRepositoryPort, deckService);
    }

    @Bean
    public ArchiveDeckUseCase archiveDeckUseCase(DeckRepositoryPort deckRepositoryPort) {
        return new ArchiveDeckUseCase(deckRepositoryPort);
    }

    @Bean
    public DeleteDeckUseCase deleteDeckUseCase(DeckRepositoryPort deckRepositoryPort) {
        return new DeleteDeckUseCase(deckRepositoryPort);
    }

    @Bean
    public FindUserDecksUseCase findUserDecksUseCase(DeckRepositoryPort deckRepositoryPort) {
        return new FindUserDecksUseCase(deckRepositoryPort);
    }

    @Bean
    public GetDeckDetailsUseCase getDeckDetailsUseCase(DeckRepositoryPort deckRepositoryPort) {
        return new GetDeckDetailsUseCase(deckRepositoryPort);
    }
}


