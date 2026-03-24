package com.lernix.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.lernix.domain.enums.CardState;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Infrastructure JPA Entity for Card persistence.
 * Optimized for SRS (SM-2) query performance and Tag indexing.
 */
@Entity
@Table(name = "cards", indexes = {
        @Index(name = "idx_card_deck", columnList = "deck_id"),
        @Index(name = "idx_card_next_review", columnList = "next_review_date"),
        @Index(name = "idx_card_state", columnList = "state")
})
@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CardEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String front;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String back;

    // --- SRS (SM-2) Fields ---

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private CardState state;

    @Column(name = "ease_factor", nullable = false)
    private double easeFactor;

    @Column(name = "review_interval", nullable = false)
    private int reviewInterval;

    @Column(name = "repetitions", nullable = false)
    private int repetitions;

    @NotNull
    @Column(name = "next_review_date", nullable = false)
    private Instant nextReviewDate;

    // --- Tag System (Many-to-Many Logic) ---
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "card_tags",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();

    // --- Audit Fields ---

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cards_deck"))
    private DeckEntity deck;

    // --- Technical overrides ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


