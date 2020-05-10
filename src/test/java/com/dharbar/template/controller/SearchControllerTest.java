package com.dharbar.template.controller;

import com.dharbar.template.service.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

@ActiveProfiles("local")
@AutoConfigureWebTestClient
class SearchControllerTest extends IntegrationTest {

    private static final String TEST_TOKEN = "test token";

    @Test
    void search_return500_artistNotProvided() {
        webTestClient.get()
                .uri("/search?artist=")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();
    }

    @Test
    void getByUserId_return500_userIdNotFound() {
        wireMockServer.stubFor(get("/search")
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())));

        webTestClient.get()
                .uri("/search?artist=test")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();
    }

}
