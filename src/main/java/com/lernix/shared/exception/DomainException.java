package com.lernix.shared.exception;

/**
 * Base class for all business domain exceptions.
 * Represents a violation of business invariants.
 */
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}

