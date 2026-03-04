package com.lernix.application.usecase.card;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.shared.exception.CardNotFoundException;

/**
 * Use Case to fetch a single card's details.
 */
public class GetCardDetailsUseCase {

    private final CardRepositoryPort cardRepositoryPort;

    public GetCardDetailsUseCase(CardRepositoryPort cardRepositoryPort) {
        this.cardRepositoryPort = cardRepositoryPort;
    }

    public Card execute(CardId cardId) {
        return cardRepositoryPort.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + cardId.value()));
    }
}

