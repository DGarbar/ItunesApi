package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.SongResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsAttributesPreview;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongsData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Primary
@Slf4j
@Service
public class DevApiItunesResource implements SongResource {

    private final DevApiItunesRequestSearcher devApiItunesRequester;
    private final DevApiItunesMusicAttributesMapper musicAttributesMapper;

    public DevApiItunesResource(DevApiItunesRequestSearcher devApiItunesRequester,
                                DevApiItunesMusicAttributesMapper musicAttributesMapper) {
        this.devApiItunesRequester = devApiItunesRequester;
        this.musicAttributesMapper = musicAttributesMapper;
    }

    @Override
    public Flux<MusicAsResource> findBy(String search) {
        if (StringUtils.isBlank(search)) {
            return Flux.error(() -> new IllegalArgumentException("Search param is blank"));
        }

        var params = musicAttributesMapper.mapSong(search);
        return devApiItunesRequester.request(params)
                .flatMapMany(this::extractData)
                .map(this::mapToDto);
    }

    @Override
    public Mono<MusicAsResource> findMelody(String artist, String songName) {
        if (StringUtils.isAnyBlank(artist, songName)) {
            return Mono.error(() -> new IllegalArgumentException("Artist or songName param is blank"));
        }

        var artistAndSong = String.format("%s %s", artist, songName);
        var params = musicAttributesMapper.mapSong(artistAndSong);
        return devApiItunesRequester.request(params)
                .flatMapMany(this::extractData)
                .take(1)
                .singleOrEmpty()
                .map(this::mapToDto);
    }

    private Flux<ItunesSongsData> extractData(ItunesResult itunesResult) {
        var itunesSongs = itunesResult == null ? null : itunesResult.getSongs();
        var itunesSongsData = itunesSongs == null ? null : itunesSongs.getData();
        return CollectionUtils.isEmpty(itunesSongsData) ? Flux.empty() : Flux.fromIterable(itunesSongsData);
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
