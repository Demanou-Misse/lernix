package com.lernix.application.usecase.card;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.Tag;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.CardRepositoryPort;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Use Case to retrieve cards filtered by a specific Tag for a specific User.
 * Essential for "Targeted Study Sessions".
 */
public class GetCardsByTagUseCase {

    private final CardRepositoryPort cardRepository;

    public GetCardsByTagUseCase(CardRepositoryPort cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Retrieves only cards that are DUE for review and have the specified tag.
     */
    public List<Card> execute(UserId userId, String tagName, boolean onlyDue) {
        Objects.requireNonNull(userId, "User ID is required");
        Tag tag = new Tag(tagName); // Self-validates the tag format

        if (onlyDue) {
            return cardRepository.findDueByTag(userId, tag, Instant.now());
        }
        return cardRepository.findAllByTag(userId, tag);
    }
}

