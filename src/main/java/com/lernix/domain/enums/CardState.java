package com.lernix.domain.enums;

/**
 * Represents the lifecycle stages of a flashcard within the Spaced Repetition System.
 * Transitions are governed by the SpacedRepetitionEngine.
 */
public enum CardState {
    /** New card, never reviewed by the user. */
    NEW,

    /** Intensive learning phase with short-term intervals. */
    LEARNING,

    /** Graduated card with long-term expanded intervals. */
    REVIEW,

    /** Card was forgotten during REVIEW and returned to intensive learning. */
    RELEARNING
}

