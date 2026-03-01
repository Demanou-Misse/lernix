package com.lernix.shared.exception;

public class DeckNotFoundException extends DomainException {
    public DeckNotFoundException(String message) {
        super(message);
    }
}

