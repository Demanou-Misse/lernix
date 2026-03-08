package com.lernix.infrastructure.config;

import com.lernix.application.usecase.card.*;
import com.lernix.application.usecase.user.*;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.domain.service.DeckService;
import com.lernix.domain.service.PasswordHasher;
import com.lernix.application.usecase.deck.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Use Case Configuration.
 * Orchestrates the wiring of Pure Java Use Cases with Infrastructure Adapters.
 * Following the Dependency Inversion Principle (DIP).
 */
@Configuration
public class UseCaseConfig {

    // --- USER MANAGEMENT ---

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordHasher passwordHasher) {
        return new CreateUserUseCase(userRepositoryPort, passwordHasher);
    }

    @Bean
    public GetUserProfileUseCase getUserProfileUseCase(UserRepositoryPort userRepositoryPort) {
        return new GetUserProfileUseCase(userRepositoryPort);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepositoryPort userRepositoryPort, PasswordHasher passwordHasher) {
        return new ChangePasswordUseCase(userRepositoryPort, passwordHasher);
    }

    @Bean
    public UpdateUserEmailUseCase updateUserEmailUseCase(UserRepositoryPort userRepositoryPort) {
        return new UpdateUserEmailUseCase(userRepositoryPort);
    }

    @Bean
    public DisableUserAccountUseCase disableUserAccountUseCase(UserRepositoryPort userRepositoryPort) {
        return new DisableUserAccountUseCase(userRepositoryPort);
    }

    @Bean
    public com.lernix.usecase.user.PurgeUserAccountUseCase purgeUserAccountUseCase(UserRepositoryPort userRepositoryPort) {
        return new com.lernix.usecase.user.PurgeUserAccountUseCase(userRepositoryPort);
    }

    @Bean
    public GetAccountStatisticsUseCase getAccountStatisticsUseCase(
            UserRepositoryPort userRepositoryPort,
            DeckRepositoryPort deckRepositoryPort,
            CardRepositoryPort cardRepositoryPort) {
        return new GetAccountStatisticsUseCase(userRepositoryPort, deckRepositoryPort, cardRepositoryPort);
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

    // --- CARD USE CASES ---
    @Bean
    public CreateCardUseCase createCardUseCase(CardRepositoryPort cardRepositoryPort, DeckRepositoryPort deckRepositoryPort) {
        return new CreateCardUseCase(cardRepositoryPort, deckRepositoryPort);
    }

    @Bean
    public UpdateCardContentUseCase updateCardContentUseCase(CardRepositoryPort cardRepositoryPort) {
        return new UpdateCardContentUseCase(cardRepositoryPort);
    }

    @Bean
    public GetDeckCardsUseCase getDeckCardsUseCase(CardRepositoryPort cardRepositoryPort, DeckRepositoryPort deckRepositoryPort) {
        return new GetDeckCardsUseCase(cardRepositoryPort, deckRepositoryPort);
    }

    @Bean
    public GetCardDetailsUseCase getCardDetailsUseCase(CardRepositoryPort cardRepositoryPort) {
        return new GetCardDetailsUseCase(cardRepositoryPort);
    }

    @Bean
    public DeleteCardUseCase deleteCardUseCase(CardRepositoryPort cardRepositoryPort) {
        return new DeleteCardUseCase(cardRepositoryPort);
    }
}


