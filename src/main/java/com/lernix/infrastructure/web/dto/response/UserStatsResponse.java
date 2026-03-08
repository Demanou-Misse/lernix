package com.lernix.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Account usage metrics")
public record UserStatsResponse(
        @Schema(description = "Total number of decks owned")
        long totalDecks,

        @Schema(description = "Total number of cards created across all decks")
        long totalCards
) {}

