package com.lernix.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Public representation of a Deck")
public record DeckResponse(
        UUID id,
        UUID ownerId,
        String title,
        String description,
        String status,
        Instant createdAt,
        Instant updatedAt
) {}

