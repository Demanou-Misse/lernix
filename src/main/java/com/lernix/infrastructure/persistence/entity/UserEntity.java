package com.lernix.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;
import java.util.UUID;

/**
 * Infrastructure JPA Entity for User persistence.
 * Optimized for PostgreSQL and following 2026 Enterprise performance standards.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email")
})
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA Requirement
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder use only
public class UserEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NaturalId // Business key optimization for Hibernate
    @Email
    @NotNull
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Standard for 2026: Manual versioning for optimistic locking
    @Version
    private Long version;

    /**
     * Strategic Equals/HashCode: In JPA, we must use the Business Key (UUID)
     * instead of all fields to ensure stability in Sets and Map across states.
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


