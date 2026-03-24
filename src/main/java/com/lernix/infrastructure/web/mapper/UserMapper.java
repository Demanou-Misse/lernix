package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.enums.UserStatus;
import com.lernix.domain.model.*;
import com.lernix.infrastructure.persistence.entity.UserEntity;
import com.lernix.infrastructure.web.dto.response.UserProfileResponse;
import org.springframework.stereotype.Component;

/**
 * Maps User domain aggregate to web-friendly DTOs.
 * Ensures sensitive data like password hashes never leak to the API.
 */
@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
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

    public UserEntity toEntity(User domain) {
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

    public UserProfileResponse toResponse(User domain) {
        return new UserProfileResponse(
                domain.id().value(),
                domain.email().value(),      // Unwraps the Email Value Object
                domain.status().name(),     // Enum to String for JSON stability
                domain.createdAt(),
                domain.updatedAt()
        );
    }
}


