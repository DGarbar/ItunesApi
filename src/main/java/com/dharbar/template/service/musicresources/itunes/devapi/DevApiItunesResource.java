package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.MusicResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesSongsAttributesPreview;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesSongsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Primary
@Slf4j
@Service
public class DevApiItunesResource implements MusicResource {

    private final DevApiItunesRequester openApiItunesRequester;
    private final DevApiItunesMusicAttributesMapper musicAttributesMapper;

    public DevApiItunesResource(DevApiItunesRequester openApiItunesRequester,
                                DevApiItunesMusicAttributesMapper musicAttributesMapper) {
        this.openApiItunesRequester = openApiItunesRequester;
        this.musicAttributesMapper = musicAttributesMapper;
    }

    @Override
    public Flux<MusicAsResource> findByArtist(String artist) {
        var params = musicAttributesMapper.mapArtist(artist);
        return openApiItunesRequester.request(params)
                .map(this::mapToDto);
    }

    @Override
    public Mono<MusicAsResource> findMelody(String artist, String songName) {
        var params = musicAttributesMapper.mapMelody(artist, songName);
        return openApiItunesRequester.requestOne(params)
                .map(this::mapToDto);
    }

    @Override
    public Flux<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes) {
        var params = musicAttributesMapper.mapMusicAttributes(musicAttributes);
        return openApiItunesRequester.request(params)
                .map(this::mapToDto);
    }

    private MusicAsResource mapToDto(ItunesSongsData itunesSongsData) {
        var itunesSongsAttributes = itunesSongsData.getAttributes();
        var fileUrl = itunesSongsAttributes.getPreviews().stream()
                .map(ItunesSongsAttributesPreview::getUrl)
                .findAny()
                .orElse(null);
        return MusicAsResource.builder()
                .artist(itunesSongsAttributes.getArtistName())
                .songName(itunesSongsAttributes.getName())
                .fileUrl(fileUrl)
                .trackTimeMillis(itunesSongsAttributes.getDurationInMillis())
                .genres(itunesSongsAttributes.getGenreNames())
                .build();
    }
}
