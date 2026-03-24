package com.lernix.domain.model;

import com.lernix.domain.algorithm.SpacedRepetitionEngine;
import com.lernix.domain.enums.CardState;
import com.lernix.domain.enums.ReviewGrade;
import com.lernix.domain.algorithm.ReviewResult;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Domain Aggregate Root for a Flashcard.
 * Controlled mutation of internal state via explicit business methods.
 */
public class Card {
    private final CardId id;
    private final DeckId deckId;
    private CardContent content;
    private CardState state;
    private ReviewMetaData reviewMetaData;
    private Set<Tag> tags;
    private final Instant createdAt;
    private Instant updatedAt;

    // --- Constructor for Reconstruction (Used by Repository/Mapper) ---
    public Card(CardId id, DeckId deckId, CardContent content, CardState state,
                ReviewMetaData reviewMetaData, Set<Tag> tags, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id);
        this.deckId = Objects.requireNonNull(deckId);
        this.content = Objects.requireNonNull(content);
        this.state = Objects.requireNonNull(state);
        this.reviewMetaData = Objects.requireNonNull(reviewMetaData);
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    /**
     * Factory method for creating a brand-new Card.
     * Note: A new card starts in NEW state with initial metadata.
     */
    public static Card create(DeckId deckId, String front, String back, Set<Tag> tags) {
        Instant now = Instant.now();
        return new Card(
                CardId.generate(),
                deckId,
                new CardContent(front, back),
                CardState.NEW,
                ReviewMetaData.createInitial(), // EF 2.5, Interval 0, Reps 0
                tags,
                now,
                now
        );
    }

    /**
     * BUSINESS LOGIC: The core of Issue #6.
     * Applies the spaced repetition algorithm to determine the next review state.
     */
    public void applyReview(ReviewGrade grade, SpacedRepetitionEngine engine) {
        // Math is delegated to the engine, but the Card manages the update
        ReviewResult result = engine.calculateNextReview(this.state, this.reviewMetaData, grade);

        this.state = result.nextState();
        this.reviewMetaData = result.nextMetaData();
        this.updatedAt = Instant.now();
    }

    /**
     * Tag Management: Your flagship feature.
     */
    public void updateTags(Set<Tag> newTags) {
        this.tags = new HashSet<>(newTags);
        this.updatedAt = Instant.now();
    }

    public Card updateContent(String newFront, String newBack) {
        return new Card(
                this.id,
                this.deckId,
                new CardContent(newFront, newBack),
                this.state,
                this.reviewMetaData,
                this.tags,
                this.createdAt,
                Instant.now()
        );
    }


    // --- Getters (Standard Encapsulation) ---
    public CardId getId() { return id; }
    public DeckId getDeckId() { return deckId; }
    public CardContent getContent() { return content; }
    public CardState getState() { return state; }
    public ReviewMetaData getReviewMetaData() { return reviewMetaData; }
    public Set<Tag> getTags() { return Collections.unmodifiableSet(tags); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}


