package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for updating an existing deck")
public record UpdateDeckRequest(
        @NotBlank @Size(min = 3, max = 50) String title,
        @Size(max = 255) String description
) {}

