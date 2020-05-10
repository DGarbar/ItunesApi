package com.dharbar.template.controller;

import com.dharbar.template.service.IntegrationTest;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.devapi.token.TokenProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("local")
class MusicControllerTest extends IntegrationTest {

    @MockBean
    private TokenProvider tokenProvider;

    @Test
    void music_return500_searchNotProvided() {
        webTestClient.get()
                .uri("/music?search=")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();
    }

    @SneakyThrows
    @Test
    void music_return500_serviceUnauthorized() {
        given(tokenProvider.provideToken()).willReturn("test");
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("types", equalTo("songs"))
                .withQueryParam("term", equalTo("test"))
                .withHeader("Authorization", equalTo("Bearer test"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())));

        webTestClient.get()
                .uri("/music?search=test")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody();
    }

    @SneakyThrows
    @Test
    void music_returnNothing_serviceIsRequestedEmpty() {
        given(tokenProvider.provideToken()).willReturn("test");
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("types", equalTo("songs"))
                .withQueryParam("term", equalTo("test"))
                .withHeader("Authorization", equalTo("Bearer test"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"results\": {}}")));

        webTestClient.get()
                .uri("/music?search=test")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MusicAsResource.class).hasSize(0);
    }

    @SneakyThrows
    @Test
    void music_returnParsedMusic_serviceIsReturned() {
        given(tokenProvider.provideToken()).willReturn("test");
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("types", equalTo("songs"))
                .withQueryParam("term", equalTo("test"))
                .withHeader("Authorization", equalTo("Bearer test"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonFrom("/it/itunes/response/songsResponse.json"))));

        webTestClient.get()
                .uri("/music?search=test")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(jsonFrom("/it/musicSearcher/songsParsed.json"));
    }
}
