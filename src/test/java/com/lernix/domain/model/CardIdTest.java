package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain: CardId Unit Tests")
class CardIdTest {

    @Test
    @DisplayName("Should generate a valid random CardId")
    void shouldGenerateValidId() {
        CardId id = CardId.generate();
        assertNotNull(id.value());
    }

    @Test
    @DisplayName("Should throw exception if value is null")
    void shouldThrowExceptionWhenNull() {
        assertThrows(NullPointerException.class, () -> new CardId(null));
    }
}

