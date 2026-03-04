package com.lernix.application.usecase.card;

import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.shared.exception.CardNotFoundException;
import com.lernix.shared.exception.DomainException;

/**
 * Use Case to modify the front/back content of an existing card.
 */
public class UpdateCardContentUseCase {

    private final CardRepositoryPort cardRepositoryPort;

    public UpdateCardContentUseCase(CardRepositoryPort cardRepositoryPort) {
        this.cardRepositoryPort = cardRepositoryPort;
    }

    public Card execute(CardId cardId, String newFront, String newBack) {
        // 1. Fetch existing card with precise exception
        Card existingCard = cardRepositoryPort.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardId.value()));

        // 2. Update via domain logic (produces new instance)
        Card updatedCard = existingCard.updateContent(newFront, newBack);

        // 3. Save the new state
        return cardRepositoryPort.save(updatedCard);
    }

}

