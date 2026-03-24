package com.lernix.application.usecase.card;

import com.lernix.domain.algorithm.SpacedRepetitionEngine;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.model.Card;
import com.lernix.domain.model.CardId;
import com.lernix.domain.ports.CardRepositoryPort;
import com.lernix.shared.exception.CardNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Pure Java Use Case. No @Service annotation here.
 * Wired manually in UseCaseConfig.
 */
public class ProcessCardReviewUseCase {

    private final CardRepositoryPort cardRepository;
    private final SpacedRepetitionEngine repetitionEngine;

    public ProcessCardReviewUseCase(CardRepositoryPort cardRepository,
                                    SpacedRepetitionEngine repetitionEngine) {
        this.cardRepository = cardRepository;
        this.repetitionEngine = repetitionEngine;
    }

    @Transactional // Transactional boundary defined at the Use Case level
    public Card execute(CardId cardId, ReviewGrade grade) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + cardId.value()));

        card.applyReview(grade, repetitionEngine);

        return cardRepository.save(card);
    }
}


