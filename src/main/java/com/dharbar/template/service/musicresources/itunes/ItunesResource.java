package com.dharbar.template.service.musicresources.itunes;

import com.dharbar.template.service.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.MusicResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import com.dharbar.template.service.musicresources.itunes.dto.ItunesResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class ItunesResource implements MusicResource {

    private final ItunesRequester itunesRequester;
    private final ItunesMusicAttributesMapper musicAttributesMapper;

    public ItunesResource(ItunesRequester itunesRequester, ItunesMusicAttributesMapper musicAttributesMapper) {
        this.itunesRequester = itunesRequester;
        this.musicAttributesMapper = musicAttributesMapper;
    }

    @Override
    public Flux<MusicAsResource> findByArtist(String artist) {
        final List<NameValuePair> params = musicAttributesMapper.mapArtist(artist);
        return itunesRequester.request(params)
                .map(this::mapToDto);
    }

    @Override
    public Mono<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes) {
        final List<NameValuePair> params = musicAttributesMapper.mapMusicAttributes(musicAttributes);
        return itunesRequester.requestOne(params)
                .map(this::mapToDto);
    }

    private MusicAsResource mapToDto(ItunesResult itunesResult) {
        return MusicAsResource.builder()
                .artist(itunesResult.getArtistName())
                .songName(itunesResult.getTrackName())
                .fileUrl(itunesResult.getPreviewUrl())
                .trackTimeMillis(itunesResult.getTrackTimeMillis())
                .genre(itunesResult.getPrimaryGenreName())
                .build();
    }
}
