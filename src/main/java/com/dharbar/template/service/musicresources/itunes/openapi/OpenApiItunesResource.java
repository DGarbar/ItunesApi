package com.dharbar.template.service.musicresources.itunes.openapi;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.MusicResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.openapi.dto.openapi.ItunesResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OpenApiItunesResource implements MusicResource {

    private final OpenApiItunesRequester openApiItunesRequester;
    private final OpenApiItunesMusicAttributesMapper musicAttributesMapper;

    public OpenApiItunesResource(OpenApiItunesRequester openApiItunesRequester,
                                 OpenApiItunesMusicAttributesMapper musicAttributesMapper) {
        this.openApiItunesRequester = openApiItunesRequester;
        this.musicAttributesMapper = musicAttributesMapper;
    }

    @Override
    public Flux<MusicAsResource> findByArtist(String artist) {
        final List<NameValuePair> params = musicAttributesMapper.mapArtist(artist);
        return openApiItunesRequester.request(params)
                .map(this::mapToDto);
    }

    @Override
    public Mono<MusicAsResource> findMelody(String artist, String songName) {
        final List<NameValuePair> params = musicAttributesMapper.mapMelody(artist, songName);
        return openApiItunesRequester.requestOne(params)
                .map(this::mapToDto);
    }

    @Override
    public Flux<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes) {
        final List<NameValuePair> params = musicAttributesMapper.mapMusicAttributes(musicAttributes);
        return openApiItunesRequester.request(params)
                .map(this::mapToDto);
    }

    private MusicAsResource mapToDto(ItunesResult itunesResult) {
        return MusicAsResource.builder()
                .artist(itunesResult.getArtistName())
                .songName(itunesResult.getTrackName())
                .fileUrl(itunesResult.getPreviewUrl())
                .trackTimeMillis(itunesResult.getTrackTimeMillis())
                .genres(Collections.singletonList(itunesResult.getPrimaryGenreName()))
                .build();
    }
}
