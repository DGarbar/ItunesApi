package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongs;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsAttributes;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsAttributesPreview;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsData;
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
import static org.mockito.Mockito.verifyNoInteractions;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@ExtendWith(MockitoExtension.class)
class DevApiItunesResourceTest {

    @Mock
    private DevApiItunesRequestSearcher devApiItunesRequester;

    @Mock
    private DevApiItunesMusicAttributesMapper musicAttributesMapper;

    @InjectMocks
    private DevApiItunesResource target;

    @Test
    void findMelody_fluxError_songIsBlank() {
        // when and then
        StepVerifier.create(target.findMelody("", "test"))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void findMelody_fluxError_artistIsBlank() {
        // when and then
        StepVerifier.create(target.findMelody("test", ""))
                .verifyError(IllegalArgumentException.class);

        verifyNoInteractions(musicAttributesMapper);
    }

    @Test
    void findMelody_returnError_requestIsFailed() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());
        given(devApiItunesRequester.request(any())).willReturn(Mono.error(new IllegalArgumentException("Fail")));

        // when and then
        StepVerifier.create(target.findMelody("test", "testSong"))
                .verifyError(IllegalArgumentException.class);

        verify(musicAttributesMapper).mapSong("test testSong");
    }

    @Test
    void findMelody_returnEmpty_responseIsEmptyResult() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(null, null)));

        // when and then
        StepVerifier.create(target.findMelody("test", "testSong"))
                .expectNextCount(0)
                .verifyComplete();

        verify(musicAttributesMapper).mapSong("test testSong");
    }

    @Test
    void findMelody_returnMapped_responseWithSingleResult() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());

        var previews = List.of(ItunesSongsAttributesPreview.of("previewUrl"));
        var songsAttributes = ItunesSongsAttributes.builder().artistName("testName").previews(previews).build();
        var songsData = List.of(ItunesSongsData.of(null, null, null, songsAttributes));
        var itunesArtists = ItunesSongs.of(null, null, songsData);
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(itunesArtists, null)));

        // when and then
        var expected = MusicAsResource.builder().artist("testName").fileUrl("previewUrl").build();
        StepVerifier.create(target.findMelody("test", "testSong"))
                .expectNext(expected)
                .verifyComplete();

        verify(musicAttributesMapper).mapSong("test testSong");
    }

    @Test
    void findMelody_returnMapped_responseWithMultipleResult() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());

        var previews = List.of(ItunesSongsAttributesPreview.of("previewUrl1"));
        var songsAttributes1 = ItunesSongsAttributes.builder().artistName("testName1").previews(previews).build();
        var itunesSongsData1 = ItunesSongsData.of(null, null, null, songsAttributes1);
        var songsAttributes2 = ItunesSongsAttributes.builder().artistName("testName2").build();
        var itunesSongsData2 = ItunesSongsData.of(null, null, null, songsAttributes2);
        var songsData = List.of(itunesSongsData1, itunesSongsData2, itunesSongsData2);

        var itunesArtists = ItunesSongs.of(null, null, songsData);
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(itunesArtists, null)));

        // when and then
        var expected = MusicAsResource.builder().artist("testName1").fileUrl("previewUrl1").build();
        StepVerifier.create(target.findMelody("test", "testSong"))
                .expectNext(expected)
                .verifyComplete();

        verify(musicAttributesMapper).mapSong("test testSong");
    }

    @Test
    void findBy_fluxError_searchIsBlank() {
        // when and then
        StepVerifier.create(target.findBy(""))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void findBy_returnError_requestIsFailed() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());
        given(devApiItunesRequester.request(any())).willReturn(Mono.error(new IllegalArgumentException("Fail")));

        // when and then
        StepVerifier.create(target.findBy("test"))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void findBy_returnEmpty_responseIsEmptyResult() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(null, null)));

        // when and then
        StepVerifier.create(target.findBy("test"))
                .expectNextCount(0)
                .verifyComplete();

        verify(musicAttributesMapper).mapSong("test");
    }

    @Test
    void findBy_returnMapped_responseWithMultipleResult() {
        // given
        given(musicAttributesMapper.mapSong(any())).willReturn(List.of());

        var previews = List.of(ItunesSongsAttributesPreview.of("previewUrl1"));
        var songsAttributes1 = ItunesSongsAttributes.builder().artistName("testName1").previews(previews).build();
        var itunesSongsData1 = ItunesSongsData.of(null, null, null, songsAttributes1);
        var songsAttributes2 = ItunesSongsAttributes.builder().artistName("testName2").previews(previews).build();
        var itunesSongsData2 = ItunesSongsData.of(null, null, null, songsAttributes2);
        var songsData = List.of(itunesSongsData1, itunesSongsData2, itunesSongsData2);

        var itunesArtists = ItunesSongs.of(null, null, songsData);
        given(devApiItunesRequester.request(any())).willReturn(Mono.just(ItunesResult.of(itunesArtists, null)));

        // when and then
        var expected1 = MusicAsResource.builder().artist("testName1").fileUrl("previewUrl1").build();
        var expected2 = MusicAsResource.builder().artist("testName2").fileUrl("previewUrl1").build();
        var expected3 = MusicAsResource.builder().artist("testName2").fileUrl("previewUrl1").build();
        StepVerifier.create(target.findBy("test"))
                .expectNext(expected1, expected2, expected3)
                .verifyComplete();

        verify(musicAttributesMapper).mapSong("test");
    }
}
