package com.lernix.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for creating a new user account")
public record CreateUserRequest(
        @Schema(example = "dev.senior@lernix.com")
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Invalid email format")
        String email,

        @Schema(example = "SecurePass123!")
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
        String password
) {}

