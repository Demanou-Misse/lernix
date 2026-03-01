package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.Email;
import com.lernix.domain.model.PasswordHash;
import com.lernix.domain.model.User;
import com.lernix.domain.model.UserId;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Concrete implementation of the UserRepositoryPort.
 * Provides full lifecycle management for User aggregates using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value())
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value())
                .map(this::toDomain);
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public boolean existsById(UserId id) {
        return jpaRepository.existsById(id.value());
    }

    // --- Private Mapping Methods (Infrastructure Detail) ---

    private UserEntity toEntity(User domain) {
        return UserEntity.builder()
                .id(domain.id().value())
                .email(domain.email().value())
                .passwordHash(domain.passwordHash().value())
                .createdAt(domain.createdAt())
                .build();
    }

    private User toDomain(UserEntity entity) {
        return new User(
                new UserId(entity.getId()),
                new Email(entity.getEmail()),
                new PasswordHash(entity.getPasswordHash()),
                entity.getCreatedAt()
        );
    }
}


