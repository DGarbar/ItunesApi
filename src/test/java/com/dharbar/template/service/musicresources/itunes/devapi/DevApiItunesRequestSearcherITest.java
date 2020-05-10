package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.IntegrationTest;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists.ItunesArtists;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongs;
import com.dharbar.template.service.musicresources.itunes.devapi.token.TokenProvider;
import com.dharbar.template.service.musicresources.itunes.devapi.token.exception.NoTokenException;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

// TODO Make this immutable
// WireMock will consume it's own service resources
//@Import(DevApiItunesRequestSearcherTest.MyTestConfiguration.class)
@ActiveProfiles("local")
class DevApiItunesRequestSearcherITest extends IntegrationTest {

    private static final String TEST_TOKEN = "test token";

    @MockBean
    private TokenProvider tokenProvider;

    @Autowired
    private DevApiItunesRequestSearcher target;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        given(tokenProvider.provideToken()).willReturn(TEST_TOKEN);
    }

    @Test
    void request_returnErrorMono_tokenException() throws NoTokenException {
        // given
        given(tokenProvider.provideToken()).willThrow(NoTokenException.class);

        // when and then
        StepVerifier.create(target.request(List.of()))
                .verifyError(NoTokenException.class);
    }

    @Test
    void request_emptyMono_unauthorized() {
        // given
        wireMockServer.stubFor(get("/search")
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())));

        // when and then
        StepVerifier.create(target.request(List.of()))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void request_emptyMono_internalServerError() {
        // given
        wireMockServer.stubFor(get("/search")
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));

        // when and then
        StepVerifier.create(target.request(List.of()))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void request_emptyResult_emptyBody() {
        // given
        wireMockServer.stubFor(get("/search")
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"results\": {}}")));

        // when and then
        StepVerifier.create(target.request(List.of()))
                .assertNext(itunesResult -> assertThat(itunesResult).isEqualTo(ItunesResult.of(null, null)))
                .verifyComplete();
    }

    @Test
    void request_createUrlWithParameters_parametersNotEmpty() {
        // given
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("testName1", equalTo("testValue1"))
                .withQueryParam("testName2", equalTo("testValue2"))
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"results\": {}}")));

        // when
        List<NameValuePair> params = List.of(
                new BasicNameValuePair("testName1", "testValue1"),
                new BasicNameValuePair("testName2", "testValue2"));
        Mono<ItunesResult> request = target.request(params);

        // then
        StepVerifier.create(request)
                .assertNext(itunesResult -> assertThat(itunesResult).isEqualTo(ItunesResult.of(null, null)))
                .verifyComplete();
    }

    @Test
    void request_parseSongs_songsWasReturned() {
        // given
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("term", equalTo("songs"))
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonFrom("/it/itunes/response/songsResponse.json"))));

        // when
        Mono<ItunesResult> request = target.request(List.of(new BasicNameValuePair("term", "songs")));

        // then
        StepVerifier.create(request)
                .assertNext(itunesResult -> assertThat(itunesResult)
                        .extracting(ItunesResult::getSongs)
                        .extracting(ItunesSongs::getData)
                        .extracting(List::size)
                        .isEqualTo(2))
                .verifyComplete();
    }

    @Test
    void request_parseArtists_ArtistsWasReturned() {
        // given
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("term", equalTo("artists"))
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonFrom("/it/itunes/response/artistsResponse.json"))));

        // when
        Mono<ItunesResult> request = target.request(List.of(new BasicNameValuePair("term", "artists")));

        // then
        StepVerifier.create(request)
                .assertNext(itunesResult -> assertThat(itunesResult)
                        .extracting(ItunesResult::getArtists)
                        .extracting(ItunesArtists::getData)
                        .extracting(List::size)
                        .isEqualTo(2))
                .verifyComplete();
    }

    @Test
    void request_parseSongsAndArtists_songsAndArtistsWasReturned() {
        // given
        wireMockServer.stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("term", equalTo("artists,songs"))
                .withHeader("Authorization", equalTo("Bearer " + TEST_TOKEN))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonFrom("/it/itunes/response/artistsAndSongsResponse.json"))));

        // when
        Mono<ItunesResult> request = target.request(List.of(new BasicNameValuePair("term", "artists,songs")));

        // then
        StepVerifier.create(request)
                .assertNext(itunesResult -> assertThat(itunesResult)
                        .satisfies(result -> {
                            assertThat(result.getSongs().getData()).hasSize(2);
                            assertThat(result.getArtists().getData()).hasSize(2);
                        }))
                .verifyComplete();
    }
}
