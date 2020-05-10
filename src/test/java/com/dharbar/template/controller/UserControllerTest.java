package com.dharbar.template.controller;

import com.dharbar.template.service.IntegrationTest;
import com.dharbar.template.service.userService.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
class UserControllerTest extends IntegrationTest {

    @Test
    void getByUserId_return404_userIdNotFound() {
        webTestClient.get()
                .uri("/users/100")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody();
    }

    @Test
    void getByUserId_returnUser_userIdFound() {
        User result = webTestClient.get()
                .uri("/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class).returnResult().getResponseBody();

        assertThat(result).isEqualTo(User.of("test", "http://test"));
    }
}
