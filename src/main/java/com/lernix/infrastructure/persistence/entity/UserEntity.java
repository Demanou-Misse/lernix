package com.lernix.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Infrastructure JPA Entity for User persistence.
 * Updated for Issue #5: Account Management & Audit.
 * Optimized for PostgreSQL with Business Key (NaturalId) and Optimistic Locking.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NaturalId
    @Email
    @NotNull
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status; // Mapped from UserStatus Enum in Adapter

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt; // Audit trail for account modifications

    @Version
    @Column(name = "version")
    private Long version; // Concurrency control (Optimistic Locking)

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<DeckEntity> decks = new HashSet<>();

    /**
     * Strategic Equals/HashCode: Using the Business Key (UUID).
     * Ensures object stability in Sets (like 'decks') across JPA states.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}



