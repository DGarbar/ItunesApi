package com.dharbar.template.service.musicresources;

import com.dharbar.template.service.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MusicResource {

    Flux<MusicAsResource> findByArtist(String artist);

    Mono<MusicAsResource> findByMusicAttributes(MusicAttributes musicAttributes);
}
