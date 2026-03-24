package com.lernix.domain.algorithm;

import com.lernix.domain.enums.CardState;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.model.ReviewMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Algorithm: SM-2 Logic Implementation")
class SM2EngineTest {

    private SM2Engine engine;

    @BeforeEach
    void setUp() {
        engine = new SM2Engine();
    }

    @Test
    @DisplayName("NEW card + EASY grade -> Immediate graduation to REVIEW (4 days)")
    void testNewToReviewGraduation() {
        ReviewResult result = engine.calculateNextReview(
                CardState.NEW,
                ReviewMetaData.createInitial(),
                ReviewGrade.EASY
        );

        assertThat(result.nextState()).isEqualTo(CardState.REVIEW);
        assertThat(result.nextMetaData().interval()).isEqualTo(4);
        assertThat(result.nextMetaData().easeFactor()).isEqualTo(2.65);
    }

    @Test
    @DisplayName("REVIEW card + AGAIN grade -> Reset to RELEARNING with EF penalty")
    void testReviewFailure() {
        ReviewMetaData current = new ReviewMetaData(2.5, 10, 5, Instant.now());

        ReviewResult result = engine.calculateNextReview(CardState.REVIEW, current, ReviewGrade.AGAIN);

        assertThat(result.nextState()).isEqualTo(CardState.RELEARNING);
        assertThat(result.nextMetaData().interval()).isEqualTo(1);
        assertThat(result.nextMetaData().easeFactor()).isEqualTo(2.0); // 2.5 - 0.5
    }

    @Test
    @DisplayName("RELEARNING card + GOOD grade -> Recovery with 20% of old interval")
    void testRelearningRecovery() {
        // Card was forgotten at interval 50
        ReviewMetaData current = new ReviewMetaData(2.5, 50, 0, Instant.now());

        ReviewResult result = engine.calculateNextReview(CardState.RELEARNING, current, ReviewGrade.GOOD);

        assertThat(result.nextState()).isEqualTo(CardState.REVIEW);
        assertThat(result.nextMetaData().interval()).isEqualTo(10); // 50 * 0.2
    }
}

