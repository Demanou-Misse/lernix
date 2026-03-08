package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for secure password rotation")
public record ChangePasswordRequest(
        @Schema(description = "Current password for identity verification", example = "OldPass123!")
        @NotBlank(message = "Current password is required")
        String oldPassword,

        @Schema(description = "New password following security policy", example = "NewSecurePass2026!")
        @NotBlank(message = "New password cannot be empty")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        String newPassword
) {}


