package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DeckTitleTest {

    @Test
    @DisplayName("Should create DeckTitle when title is valid")
    void shouldCreateWhenValid() {
        String validTitle = "Modern Java 21";
        assertDoesNotThrow(() -> new DeckTitle(validTitle));
        assertEquals(validTitle, new DeckTitle(validTitle).value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "ab", "This title is way too long and exceeds the fifty characters limit clearly"})
    @DisplayName("Should throw exception when title length is invalid or blank")
    void shouldThrowExceptionWhenLengthIsInvalid(String invalidTitle) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new DeckTitle(invalidTitle));
        assertTrue(exception.getMessage().contains("characters") || exception.getMessage().contains("empty"));
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowExceptionWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> new DeckTitle(null));
    }
}

