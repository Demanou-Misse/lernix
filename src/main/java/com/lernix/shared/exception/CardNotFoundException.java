package com.lernix.shared.exception;

public class CardNotFoundException extends DomainException {
    public CardNotFoundException(String message) {
        super(message);
    }
}

