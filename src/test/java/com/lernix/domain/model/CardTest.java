package com.lernix.domain.model;

import com.lernix.domain.algorithm.ReviewResult;
import com.lernix.domain.algorithm.SpacedRepetitionEngine;
import com.lernix.domain.enums.CardState;
import com.lernix.domain.enums.ReviewGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Domain: Card Aggregate Logic")
class CardTest {

    @Test
    @DisplayName("Should initialize a NEW card with default SRS metadata")
    void shouldCreateCardWithInitialState() {
        DeckId deckId = new DeckId(UUID.randomUUID());

        Card card = Card.create(deckId, "Front", "Back", Collections.emptySet());

        assertThat(card.getState()).isEqualTo(CardState.NEW);
        assertThat(card.getReviewMetaData().easeFactor()).isEqualTo(2.5);
        assertThat(card.getReviewMetaData().interval()).isZero();
        assertThat(card.getCreatedAt()).isEqualTo(card.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update state and timestamps after a successful review")
    void shouldUpdateStateAfterReview() throws InterruptedException {
        // FIX: Ajout du paramètre tags
        Card card = Card.create(new DeckId(UUID.randomUUID()), "Q", "A", Collections.emptySet());
        Instant originalUpdate = card.getUpdatedAt();
        Thread.sleep(1);

        SpacedRepetitionEngine engine = Mockito.mock(SpacedRepetitionEngine.class);
        ReviewMetaData nextMeta = new ReviewMetaData(2.6, 4, 1, Instant.now().plusSeconds(86400));
        when(engine.calculateNextReview(any(), any(), any()))
                .thenReturn(new ReviewResult(CardState.REVIEW, nextMeta));

        card.applyReview(ReviewGrade.EASY, engine);

        assertThat(card.getState()).isEqualTo(CardState.REVIEW);
        assertThat(card.getReviewMetaData().easeFactor()).isEqualTo(2.6);
        assertThat(card.getUpdatedAt()).isAfter(originalUpdate);
    }

}


