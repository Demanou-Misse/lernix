package com.lernix.application.usecase.user;

import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.domain.ports.DeckRepositoryPort;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.shared.exception.UserNotFoundException;

/**
 * Aggregates account-level metrics for the user dashboard.
 * Designed for high performance by using SQL count queries.
 */
public class GetAccountStatisticsUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final DeckRepositoryPort deckRepositoryPort;
    private final CardRepositoryPort cardRepositoryPort;

    public GetAccountStatisticsUseCase(
            UserRepositoryPort userRepositoryPort,
            DeckRepositoryPort deckRepositoryPort,
            CardRepositoryPort cardRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.deckRepositoryPort = deckRepositoryPort;
        this.cardRepositoryPort = cardRepositoryPort;
    }

    public AccountStats execute(UserId userId) {
        // 1. Integrity check
        if (!userRepositoryPort.existsById(userId)) {
            throw new UserNotFoundException("Statistics failed: User not found.");
        }

        // 2. Execution: Fetching raw counts from specific repositories
        long totalDecks = deckRepositoryPort.countByOwnerId(userId);

        // Note: cardRepositoryPort.countByOwnerId(userId) needs to be defined in its port
        long totalCards = cardRepositoryPort.countByOwnerId(userId);

        return new AccountStats(totalDecks, totalCards);
    }

    /**
     * Immutable Data Transfer Object for account metrics.
     */
    public record AccountStats(long totalDecks, long totalCards) {}
}

