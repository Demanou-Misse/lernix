package com.lernix.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Public representation of a Flashcard")
public record CardResponse(
        UUID id,
        UUID deckId,
        String front,
        String back,
        Instant createdAt,
        Instant updatedAt
) {}

