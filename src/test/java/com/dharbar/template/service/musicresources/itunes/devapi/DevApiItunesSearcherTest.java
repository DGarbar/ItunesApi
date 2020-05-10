package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists.ItunesArtists;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists.ItunesArtistsAttributes;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists.ItunesArtistsData;
import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@ExtendWith(MockitoExtension.class)
class DevApiItunesSearcherTest {

    @Mock
    private DevApiItunesRequestSearcher devApiItunesRequester;
    @Mock
    private DevApiItunesMusicAttributesMapper musicAttributesMapper;

    @InjectMocks
    private DevApiItunesSearcher target;

    @Test
    void findArtist_returnError_artistIsBlank() {
        // when and then
        StepVerifier.create(target.findArtist(""))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void findArtist_returnError_requestIsFailed() {
        // given
        given(musicAttributesMapper.mapArtist(any())).willReturn(List.of());
        given(devApiItunesRequester.request(any())).willReturn(Mono.error(new IllegalArgumentException("Fail")));

        // when and then
        StepVerifier.create(target.findArtist("test"))
                .verifyError(IllegalArgumentException.class);

        verify(musicAttributesMapper).mapArtist("test");
    }

    @Test
    void findArtist_returnEmpty_responseIsEmpty() {
        // given
        given(musicAttributesMapper.mapArtist(any())).willReturn(List.of());
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(null, null)));

        // when and then
        StepVerifier.create(target.findArtist("test"))
                .expectNextCount(0)
                .verifyComplete();

        verify(musicAttributesMapper).mapArtist("test");
    }

    @Test
    void findArtist_returnNames_responseIsEmpty() {
        // given
        given(musicAttributesMapper.mapArtist(any())).willReturn(List.of());

        var artistsAttributes = ItunesArtistsAttributes.of("testName", null, null);
        var itunesArtists = ItunesArtists.of(null, List.of(ItunesArtistsData.of("id", null, artistsAttributes, null)));
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(null, itunesArtists)));

        // when and then
        StepVerifier.create(target.findArtist("test"))
                .expectNext("testName")
                .verifyComplete();

        verify(musicAttributesMapper).mapArtist("test");
    }
}
