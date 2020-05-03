package com.dharbar.template.service.musicresources;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SongResource {

    Mono<MusicAsResource> findMelody(String artist, String songName);

    Flux<MusicAsResource> findByArtist(String artist);

    Flux<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes);
}
