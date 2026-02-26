package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Should create a valid user aggregate via factory method")
    void shouldCreateValidUser() {
        Email email = new Email("user@lernix.io");
        PasswordHash hash = new PasswordHash("$2a$12$V.vR8Vv/LpY.5...EXAMPLE"); // Valid mock hash

        User user = User.create(email, hash);

        assertNotNull(user.id());
        assertEquals(email, user.email());
        assertEquals(hash, user.passwordHash());
        assertTrue(user.createdAt().isBefore(Instant.now().plusSeconds(1)));
    }
}

