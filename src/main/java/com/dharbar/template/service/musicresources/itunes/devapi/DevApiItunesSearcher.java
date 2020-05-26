package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.MusicSearcher;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Primary
@Slf4j
@Service
public class DevApiItunesSearcher implements MusicSearcher {

    private final DevApiItunesRequestSearcher devApiItunesRequester;
    private final DevApiItunesMusicAttributesMapper musicAttributesMapper;

    public DevApiItunesSearcher(DevApiItunesRequestSearcher devApiItunesRequester,
                                DevApiItunesMusicAttributesMapper musicAttributesMapper) {
        this.devApiItunesRequester = devApiItunesRequester;
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
        if (StringUtils.isBlank(artist)) {
            return Flux.error(() -> new IllegalArgumentException("Artist param is blank"));
        }

        var params = musicAttributesMapper.mapArtist(artist);
        return devApiItunesRequester.request(params)
                .flatMapMany(this::extractArtistNames);
    }

//    private Flux<String> extractSongNames(ItunesResult itunesResult) {
//        return Flux.fromStream(itunesResult.getSongs().getData().stream()
//                .map(itunesArtistsData -> itunesArtistsData.getAttributes().getName()));
//    }

    private Flux<String> extractArtistNames(ItunesResult itunesResult) {
        var itunesArtists = itunesResult == null ? null : itunesResult.getArtists();
        var itunesArtistsData = itunesArtists == null ? null : itunesArtists.getData();
        if (CollectionUtils.isEmpty(itunesArtistsData)) {
            return Flux.empty();
        }

        return Flux.fromStream(itunesArtistsData.stream()
                .map(itunesArtistData -> itunesArtistData.getAttributes().getName()));
    }
}
