package com.lernix.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Infrastructure JPA Entity for Card persistence.
 * Optimized for PostgreSQL 17+.
 */
@Entity
@Table(name = "cards", indexes = {
        @Index(name = "idx_card_deck", columnList = "deck_id")
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

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Relationship: Many cards belong to one Deck
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cards_deck"))
    private DeckEntity deck;

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

