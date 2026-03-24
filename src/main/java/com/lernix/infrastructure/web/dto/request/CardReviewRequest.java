package com.lernix.infrastructure.web.dto.request;

import com.lernix.domain.enums.ReviewGrade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for submitting a card review.
 * Using Domain Enums directly in DTOs for strict type safety.
 */
@Schema(description = "Payload to process a card review session")
public record CardReviewRequest(

        @Schema(description = "User feedback grade", example = "GOOD")
        @NotNull(message = "Review grade is mandatory")
        ReviewGrade grade

) {}

