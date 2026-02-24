package com.lernix.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Public user profile information")
public record UserResponse(
        @Schema(description = "Unique technical identifier")
        UUID id,

        @Schema(example = "dev.senior@lernix.com")
        String email,

        @Schema(description = "Account creation timestamp")
        Instant createdAt
) {}

