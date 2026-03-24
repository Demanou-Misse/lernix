package com.lernix.infrastructure.persistence.repository;

import com.lernix.domain.model.*;
import com.lernix.domain.ports.UserRepositoryPort;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import com.lernix.infrastructure.web.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id.value()).map(userMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email.value()).map(userMapper::toDomain);
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
}




