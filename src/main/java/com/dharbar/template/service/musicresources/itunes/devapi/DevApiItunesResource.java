package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.SongResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsAttributesPreview;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Primary
@Slf4j
@Service
public class DevApiItunesResource implements SongResource {

    private final DevApiItunesRequestSearcher openApiItunesRequester;
    private final DevApiItunesMusicAttributesMapper musicAttributesMapper;

    public DevApiItunesResource(DevApiItunesRequestSearcher openApiItunesRequester,
                                DevApiItunesMusicAttributesMapper musicAttributesMapper) {
        this.openApiItunesRequester = openApiItunesRequester;
        this.musicAttributesMapper = musicAttributesMapper;
    }

    @Override
    public Flux<MusicAsResource> findByArtist(String artist) {
        var params = musicAttributesMapper.mapSong(artist);
        return openApiItunesRequester.request(params)
                .flatMapMany(this::extractData)
                .map(this::mapToDto);
    }

    @Override
    public Mono<MusicAsResource> findMelody(String artist, String songName) {
        var artistAndSong = String.format("%s %s", artist, songName);
        var params = musicAttributesMapper.mapSong(artistAndSong);
        return openApiItunesRequester.requestOne(params)
                .flatMapMany(this::extractData)
                .singleOrEmpty()
                .map(this::mapToDto);
    }

    @Override
    public Flux<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes) {
        var params = musicAttributesMapper.mapSong(musicAttributes);
        return openApiItunesRequester.request(params)
                .flatMapMany(this::extractData)
                .map(this::mapToDto);
    }

    private Flux<ItunesSongsData> extractData(ItunesResult itunesResult) {
        return Flux.fromIterable(itunesResult.getSongs().getData());
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
