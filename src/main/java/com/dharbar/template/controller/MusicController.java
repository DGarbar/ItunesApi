package com.dharbar.template.controller;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.SongResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final SongResource songResource;

    @PostMapping
    private Flux<MusicAsResource> findByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
        return songResource.findByMusicAttributes(musicAttributes);
    }

    @GetMapping
    private Flux<MusicAsResource> findBy(@RequestParam String search) {
        return songResource.findBy(search);
    }

//    @GetMapping("/artists")
//    private Mono<MusicAsResource> findByArtistAndSongName(@RequestParam String name,
//                                                          @RequestParam String songName) {
//        return songResource.findMelody(name, songName);
//    }
}
