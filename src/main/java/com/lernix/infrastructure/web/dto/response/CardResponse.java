package com.lernix.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Public representation of a Flashcard including SRS metadata")
public record CardResponse(
        UUID id,
        UUID deckId,
        String front,
        String back,
        String state, // NEW, LEARNING, REVIEW, RELEARNING
        Instant nextReview,
        Set<String> tags,
        Instant createdAt,
        Instant updatedAt
) {}


