package com.lernix.infrastructure.web.controller;

import com.lernix.domain.model.User;
import com.lernix.infrastructure.web.dto.request.CreateUserRequest;
import com.lernix.infrastructure.web.dto.response.UserResponse;
import com.lernix.infrastructure.web.mapper.UserMapper;
import com.lernix.application.usecase.CreateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user registration and profile management")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserMapper userMapper;

    @Operation(
            summary = "Register a new user",
            description = "Creates a unique user account using email and password."
    )
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "Email already registered")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid CreateUserRequest request) {
        log.info("REST request to register user: {}", request.email());

        User createdUser = createUserUseCase.execute(request.email(), request.password());

        log.info("User successfully registered with ID: {}", createdUser.id().value());
        return userMapper.toResponse(createdUser);
    }
}

