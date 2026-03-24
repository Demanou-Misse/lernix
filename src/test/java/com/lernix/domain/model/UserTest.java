package com.lernix.domain.model;

import com.lernix.domain.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain: User Aggregate Unit Tests")
class UserTest {

    private final Email testEmail = new Email("senior.dev@lernix.io");
    private final PasswordHash testHash = new PasswordHash("$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/Zwdcl6lsO2HGLVzWy");

    @Test
    @DisplayName("Should create a valid user via register factory")
    void shouldRegisterUserCorrectly() {
        User user = User.register(testEmail, testHash);

        assertAll("Initial User state",
                () -> assertNotNull(user.id()),
                () -> assertEquals(testEmail, user.email()),
                () -> assertEquals(UserStatus.ACTIVE, user.status()),
                () -> assertNotNull(user.createdAt()),
                () -> assertEquals(user.createdAt(), user.updatedAt(), "Initially, createdAt and updatedAt must be identical"),
                () -> assertNull(user.version(), "Version must be null for new users to trigger INSERT")

        );
    }

    @Test
    @DisplayName("Should update password and refresh updatedAt timestamp")
    void shouldUpdatePasswordImmutably() throws InterruptedException {
        User original = User.register(testEmail, testHash);
        PasswordHash newHash = new PasswordHash("b".repeat(60));

        // Wait 1ms to ensure a different Instant.now()
        Thread.sleep(1);
        User updated = original.changePassword(newHash);

        assertAll("Password update validation",
                () -> assertEquals(original.id(), updated.id(), "Identity must be preserved"),
                () -> assertEquals(newHash, updated.passwordHash()),
                () -> assertTrue(updated.updatedAt().isAfter(original.updatedAt()), "Audit timestamp must be refreshed"),
                () -> assertEquals(original.createdAt(), updated.createdAt(), "Creation date must be immutable")
        );
    }

    @Test
    @DisplayName("Should transition to DISABLED state")
    void shouldDisableUserAccount() throws InterruptedException { // Ajoute l'exception
        // 1. Given
        User activeUser = User.register(testEmail, testHash);

        // 2. Wait 1ms to ensure the clock moves forward
        Thread.sleep(1);

        // 3. When
        User disabledUser = activeUser.disable();

        // 4. Then
        assertAll("Account suspension",
                () -> assertEquals(UserStatus.DISABLED, disabledUser.status()),
                () -> assertTrue(disabledUser.updatedAt().isAfter(activeUser.updatedAt()),
                        "Update timestamp must be strictly after creation timestamp")
        );
    }


    @Test
    @DisplayName("Should throw NullPointerException if required fields are missing")
    void shouldEnforceNonNullConstraints() {
        assertThrows(NullPointerException.class, () ->
                new User(null, testEmail, testHash, UserStatus.ACTIVE, Instant.now(), Instant.now(), null)
        );
    }
}


