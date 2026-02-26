package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("Should create Email when format is valid")
    void shouldCreateEmailWhenValid() {
        assertDoesNotThrow(() -> new Email("test@lernix.com"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainAddress", "#@%^%#$@#$@#.com", "@example.com", "joe smith@example.com"})
    @DisplayName("Should throw exception when email format is invalid")
    void shouldThrowExceptionWhenInvalid(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }
}