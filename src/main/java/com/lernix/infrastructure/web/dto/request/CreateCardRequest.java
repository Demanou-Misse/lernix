package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateCardRequest(
        @NotBlank(message = "Deck ID is mandatory")
        String deckId,

        @NotBlank(message = "Front side content cannot be empty")
        @Size(max = 2000)
        String front,

        @NotBlank(message = "Back side content cannot be empty")
        @Size(max = 5000)
        String back,

        @Schema(description = "Optional list of tags", example = "[\"medicine\", \"anatomy\"]")
        Set<String> tags
) {}


