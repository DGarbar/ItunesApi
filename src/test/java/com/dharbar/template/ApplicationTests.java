package com.dharbar.template;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "/test-config.properties")
@ActiveProfiles("local")
@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }
}
