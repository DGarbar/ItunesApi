package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.MusicSearcher;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Primary
@Slf4j
@Service
public class DevApiItunesSearcher implements MusicSearcher {

    private final DevApiItunesRequestSearcher openApiItunesRequester;
    private final DevApiItunesMusicAttributesMapper musicAttributesMapper;

    public DevApiItunesSearcher(DevApiItunesRequestSearcher openApiItunesRequester,
                                DevApiItunesMusicAttributesMapper musicAttributesMapper) {
        this.openApiItunesRequester = openApiItunesRequester;
        this.musicAttributesMapper = musicAttributesMapper;
    }
//
//    @Override
//    public Flux<String> findSong(String songName) {
//        var params = musicAttributesMapper.mapSong(songName);
//        return openApiItunesRequester.request(params)
//                .flatMapMany(this::extractSongNames);
//    }

    @Override
    public Flux<String> findArtist(String artist) {
        var params = musicAttributesMapper.mapArtist(artist);
        return openApiItunesRequester.request(params)
                .flatMapMany(this::extractArtistNames);
    }

    private Flux<String> extractSongNames(ItunesResult itunesResult) {
        return Flux.fromIterable(itunesResult.getSongs().getData().stream()
                .map(itunesArtistsData -> itunesArtistsData.getAttributes().getName())
                .collect(Collectors.toList()));
    }

    private Flux<String> extractArtistNames(ItunesResult itunesResult) {
        return Flux.fromIterable(itunesResult.getArtists().getData().stream()
                .map(itunesArtistsData -> itunesArtistsData.getAttributes().getName())
                .collect(Collectors.toList()));
    }
}
