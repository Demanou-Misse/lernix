package com.lernix.infrastructure.web.controller;

import com.lernix.domain.model.*;
import com.lernix.infrastructure.web.dto.request.ChangePasswordRequest;
import com.lernix.infrastructure.web.dto.request.CreateUserRequest;
import com.lernix.infrastructure.web.dto.request.UpdateEmailRequest;
import com.lernix.infrastructure.web.dto.response.UserProfileResponse;
import com.lernix.infrastructure.web.dto.response.UserStatsResponse;
import com.lernix.infrastructure.web.mapper.UserMapper;
import com.lernix.shared.response.ApiResponse;
import com.lernix.application.usecase.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for User Account Management.
 * Implements GDPR-compliant workflows and secure profile management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for profile, security, and account lifecycle")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final UpdateUserEmailUseCase updateUserEmailUseCase;
    private final DisableUserAccountUseCase disableUserAccountUseCase;
    private final com.lernix.usecase.user.PurgeUserAccountUseCase purgeUserAccountUseCase;
    private final GetAccountStatisticsUseCase getAccountStatisticsUseCase;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a unique user account using email and password.")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserProfileResponse> register(@RequestBody @Valid CreateUserRequest request) {
        log.info("REST request to register user: {}", request.email());

        User createdUser = createUserUseCase.execute(request.email(), request.password());
        log.info("User successfully registered with ID: {}", createdUser.id().value());
        UserProfileResponse response = userMapper.toResponse(createdUser);

        return ApiResponse.success(response, "User successfully registered");
    }

    @GetMapping("/{id}/me")
    @Operation(summary = "Get user profile", description = "Retrieves current user details without sensitive data")
    public ApiResponse<UserProfileResponse> getProfile(@PathVariable UUID id) {
        log.debug("Fetching profile for user: {}", id);
        User user = getUserProfileUseCase.execute(new UserId(id));
        return ApiResponse.success(userMapper.toResponse(user), "Profile retrieved");
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "Get account stats", description = "Returns total count of decks and cards")
    public ApiResponse<UserStatsResponse> getStats(@PathVariable UUID id) {
        var stats = getAccountStatisticsUseCase.execute(new UserId(id));
        return ApiResponse.success(new UserStatsResponse(stats.totalDecks(), stats.totalCards()), "Stats retrieved");
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Change password", description = "Updates credentials after verifying current password")
    public ApiResponse<Void> changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordRequest request) {
        log.info("Password change requested for user: {}", id);
        changePasswordUseCase.execute(new UserId(id), request.oldPassword(), request.newPassword());
        return ApiResponse.success(null, "Password updated successfully");
    }

    @PatchMapping("/{id}/email")
    @Operation(summary = "Update email", description = "Changes account email after uniqueness validation")
    public ApiResponse<UserProfileResponse> updateEmail(@PathVariable UUID id, @RequestBody @Valid UpdateEmailRequest request) {
        log.info("Email update requested for user: {}", id);
        User updatedUser = updateUserEmailUseCase.execute(new UserId(id), request.newEmail());
        return ApiResponse.success(userMapper.toResponse(updatedUser), "Email updated successfully");
    }

    @PostMapping("/{id}/disable")
    @Operation(summary = "Disable account", description = "Soft-delete: suspends access but preserves data")
    public ApiResponse<Void> disableAccount(@PathVariable UUID id) {
        log.warn("Disabling account: {}", id);
        disableUserAccountUseCase.execute(new UserId(id));
        return ApiResponse.success(null, "Account disabled. Data preserved for grace period.");
    }

    @DeleteMapping("/{id}/purge")
    @Operation(summary = "Purge account", description = "Hard-delete: Permanently erases user and all linked data (GDPR)")
    public ApiResponse<Void> purgeAccount(@PathVariable UUID id) {
        log.error("CRITICAL: Purging account and cascading data for user: {}", id);
        purgeUserAccountUseCase.execute(new UserId(id));
        return ApiResponse.success(null, "Account and all associated data permanently erased.");
    }
}
