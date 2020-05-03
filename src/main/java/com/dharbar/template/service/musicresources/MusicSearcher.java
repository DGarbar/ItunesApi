package com.dharbar.template.service.musicresources;

import reactor.core.publisher.Flux;

public interface MusicSearcher {

//    Flux<String> findSong(String songName);

    Flux<String> findArtist(String artist);
}
