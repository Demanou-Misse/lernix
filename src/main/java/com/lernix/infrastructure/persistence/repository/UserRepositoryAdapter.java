package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Persistence Adapter for User Aggregate.
 * Enforces the mapping between Domain Value Objects and JPA Entities.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UserId id) {
        return jpaUserRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void deleteById(UserId id) {
        // Triggers the SQL CASCADE DELETE configured in Issue #3
        jpaUserRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        return jpaUserRepository.count();
    }

    // --- Enterprise Grade Mappers ---

    /**
     * Maps Domain Aggregate (Record) to Infrastructure Entity (JPA).
     */
    private UserEntity toEntity(User domain) {
        return UserEntity.builder()
                .id(domain.id().value())
                .email(domain.email().value())
                .passwordHash(domain.passwordHash().value())
                .status(domain.status().name()) // Enum -> String
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt()) // Managed by domain, persisted here
                .version(domain.version())     // Essential for Optimistic Locking
                .build();
    }

    /**
     * Maps Infrastructure Entity (JPA) to Domain Aggregate (Record).
     */
    private User toDomain(UserEntity entity) {
        return new User(
                new UserId(entity.getId()),
                new Email(entity.getEmail()),
                new PasswordHash(entity.getPasswordHash()),
                UserStatus.valueOf(entity.getStatus()), // String -> Enum
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getVersion()
        );
    }
}




