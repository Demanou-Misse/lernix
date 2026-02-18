package com.lernix.shared.exception;

/**
 * Thrown when an entity creation fails due to a uniqueness constraint violation.
 */
public class EntityAlreadyExistsException extends DomainException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}

