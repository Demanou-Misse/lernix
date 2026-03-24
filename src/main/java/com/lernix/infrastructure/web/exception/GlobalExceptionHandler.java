package com.lernix.infrastructure.web.exception;

import com.lernix.shared.exception.*;
import com.lernix.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global Exception Handler - Enterprise Standard 2026.
 * Centralizes all Domain and Infrastructure exceptions into a unified API response.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * FIX: Handles JSON parsing errors.
     * Essential for catching invalid Enum values (e.g., wrong ReviewGrade) and returning 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handlePayloadErrors(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON or invalid value: {}", ex.getLocalizedMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid input format or unrecognized value.");
    }

    /**
     * Handles @Valid annotation failures (NotBlank, Size, etc.).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Senior Tip: Collect all errors for a better developer experience (DX)
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", details);
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed: " + details);
    }

    /**
     * Handles 404 Not Found for all domain entities.
     */
    @ExceptionHandler({
            UserNotFoundException.class,
            DeckNotFoundException.class,
            CardNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleNotFound(DomainException ex) {
        log.warn("Resource mapping failed: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles 409 Conflict (Duplicate emails, titles, etc.).
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(EntityAlreadyExistsException ex) {
        log.warn("Persistence conflict: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles Security/Identity verification errors.
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidPassword(InvalidPasswordException ex) {
        log.warn("Security rejection: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Fallback for any Domain Logic violation.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Object>> handleDomainException(DomainException ex) {
        log.error("Domain logic violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * FINAL FALLBACK: Captures all unexpected system failures (True 500s).
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Throwable ex) {
        log.error("CRITICAL SYSTEM ERROR: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal error occurred.");
    }

    /**
     * Internal helper to standardize the Response Entity creation.
     */
    private ResponseEntity<ApiResponse<Object>> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(ApiResponse.error(message), status);
    }
}
