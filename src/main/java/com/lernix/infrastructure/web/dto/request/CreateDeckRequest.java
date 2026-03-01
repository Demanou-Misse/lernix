package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "Payload for creating a new card deck")
public record CreateDeckRequest(
        @Schema(description = "Owner ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank(message = "Owner ID is mandatory")
        String ownerId,

        @Schema(description = "Deck title", example = "Advanced Mathematics")
        @NotBlank(message = "Title cannot be empty")
        @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
        String title,

        @Schema(description = "Optional description", example = "Core concepts for calculus")
        @Size(max = 255)
        String description
) {}

