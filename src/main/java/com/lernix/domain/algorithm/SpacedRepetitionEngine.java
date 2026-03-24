package com.lernix.domain.algorithm;

import com.lernix.domain.enums.CardState;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.model.ReviewMetaData;

/**
 * Core Domain Policy for Spaced Repetition calculations.
 * Defines the contract for any algorithm implementation (SM-2, FSRS, etc.)
 * to determine the next review schedule.
 */
public interface SpacedRepetitionEngine {

    ReviewResult calculateNextReview(
            CardState currentState,
            ReviewMetaData currentMetadata,
            ReviewGrade grade
    );
}

