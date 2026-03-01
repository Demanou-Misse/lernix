package com.lernix.infrastructure.web.exception;

import com.lernix.shared.exception.DomainException;
import com.lernix.shared.exception.EntityAlreadyExistsException;
import com.lernix.shared.exception.UserNotFoundException;
import com.lernix.shared.exception.DeckNotFoundException;
import com.lernix.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler - Enterprise Standard 2026.
 * Captures all infrastructure and domain exceptions to return standardized JSON.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle JSR-303 Validation Errors (@Email, @NotBlank, etc.)
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        // Senior Tip: Get the first error message for clarity, or map all of them
        String errorMessage = result.getFieldErrors().getFirst().getDefaultMessage();
        log.warn("Validation failed: {}", errorMessage);
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error: " + errorMessage);
    }

    /**
     * Handle Business Conflicts (Duplicate Email, Duplicate Title)
     * Returns 409 Conflict.
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(EntityAlreadyExistsException ex) {
        log.warn("Conflict detected: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handle Resource Missing (User or Deck not found)
     * Returns 404 Not Found.
     */
    @ExceptionHandler({UserNotFoundException.class, DeckNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleNotFound(DomainException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Catch-all for any other Domain Exception.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Object>> handleDomainException(DomainException ex) {
        log.error("Domain violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Fallback for all unexpected technical errors.
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Throwable ex) {
        log.error("CRITICAL SYSTEM ERROR: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected system error occurred.");
    }

    /**
     * Centralized helper to maintain the ApiResponse structure.
     */
    private ResponseEntity<ApiResponse<Object>> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(ApiResponse.error(message), status);
    }
}


