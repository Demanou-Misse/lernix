package com.lernix.infrastructure.web.mapper;

import com.lernix.domain.model.User;
import com.lernix.infrastructure.web.dto.response.UserProfileResponse;
import org.springframework.stereotype.Component;

/**
 * Maps User domain aggregate to web-friendly DTOs.
 * Ensures sensitive data like password hashes never leak to the API.
 */
@Component
public class UserMapper {

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


