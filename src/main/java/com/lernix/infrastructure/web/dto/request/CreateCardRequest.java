package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to add a new card to a deck")
public record CreateCardRequest(
        @Schema(description = "The target Deck ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank(message = "Deck ID is mandatory")
        String deckId,

        @Schema(description = "Content for the front side", example = "What is Hexagonal Architecture?")
        @NotBlank(message = "Front side content cannot be empty")
        @Size(max = 2000, message = "Front side content is too long")
        String front,

        @Schema(description = "Content for the back side", example = "An architectural pattern that decouples the core logic...")
        @NotBlank(message = "Back side content cannot be empty")
        @Size(max = 5000, message = "Back side content is too long")
        String back
) {}

