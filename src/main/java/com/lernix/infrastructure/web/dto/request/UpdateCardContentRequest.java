package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update card content")
public record UpdateCardContentRequest(
        @NotBlank @Size(max = 2000) String front,
        @NotBlank @Size(max = 5000) String back
) {}

