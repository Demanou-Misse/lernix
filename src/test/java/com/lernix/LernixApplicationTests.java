package com.lernix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev") // Indispensable pour utiliser Postgres Docker pendant le test
class LernixApplicationTests {

    @Test
    void contextLoads() {
    }
}

