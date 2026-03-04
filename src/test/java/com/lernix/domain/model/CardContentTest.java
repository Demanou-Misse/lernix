package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain: CardContent Unit Tests")
class CardContentTest {

    @Test
    @DisplayName("Should create valid CardContent and trim whitespace")
    void shouldCreateValidContent() {
        CardContent content = new CardContent("  Front Side  ", "  Back Side  ");

        assertAll("Content properties",
                () -> assertEquals("Front Side", content.front(), "Front should be trimmed"),
                () -> assertEquals("Back Side", content.back(), "Back should be trimmed")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Should throw exception if front or back is blank")
    void shouldThrowExceptionWhenContentIsBlank(String invalidInput) {
        assertAll("Invalid inputs",
                () -> assertThrows(IllegalArgumentException.class, () -> new CardContent(invalidInput, "Valid Back")),
                () -> assertThrows(IllegalArgumentException.class, () -> new CardContent("Valid Front", invalidInput))
        );
    }
}

