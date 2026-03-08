package com.lernix.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Public representation of the user profile")
public record UserProfileResponse(
        UUID id,
        String email,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}

