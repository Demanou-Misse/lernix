package com.lernix.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Domain: UserStatus Enumeration")
class UserStatusTest {

    @Test
    @DisplayName("Should contain all required lifecycle states")
    void shouldHaveRequiredStatuses() {
        assertAll("Enum values",
                () -> assertEquals("ACTIVE", UserStatus.ACTIVE.name()),
                () -> assertEquals("DISABLED", UserStatus.DISABLED.name()),
                () -> assertEquals("PENDING", UserStatus.PENDING.name()),
                () -> assertEquals("DELETED", UserStatus.DELETED.name())
        );
    }
}

