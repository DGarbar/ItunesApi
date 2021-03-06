package com.dharbar.template.service.musicresources;

import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SongResource {

    Mono<MusicAsResource> findMelody(String artist, String songName);

    Flux<MusicAsResource> findBy(String search);

//    Flux<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes);
}
