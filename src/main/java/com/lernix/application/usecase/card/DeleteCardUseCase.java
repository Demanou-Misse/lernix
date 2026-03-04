package com.lernix.application.usecase.card;

import com.lernix.domain.model.CardId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.shared.exception.CardNotFoundException;

/**
 * Use Case to permanently remove a card.
 */
public class DeleteCardUseCase {

    private final CardRepositoryPort cardRepositoryPort;

    public DeleteCardUseCase(CardRepositoryPort cardRepositoryPort) {
        this.cardRepositoryPort = cardRepositoryPort;
    }

    public void execute(CardId cardId) {
        if (!cardRepositoryPort.existsById(cardId)) {
            throw new CardNotFoundException("Cannot delete: Card not found with ID " + cardId.value());
        }
        cardRepositoryPort.deleteById(cardId);
    }
}

