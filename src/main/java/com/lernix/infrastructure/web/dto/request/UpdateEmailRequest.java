package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for updating account email address")
public record UpdateEmailRequest(
        @Schema(description = "New unique email address", example = "architect.new@lernix.io")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String newEmail
) {}

