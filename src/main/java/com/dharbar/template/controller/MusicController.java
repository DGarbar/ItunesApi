package com.dharbar.template.controller;

import com.dharbar.template.service.dto.MusicAttributes;
import com.dharbar.template.service.musicresources.MusicResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final MusicResource musicResource;

    @GetMapping
    private Mono<MusicAsResource> findByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
        return musicResource.findByMusicAttributes(musicAttributes);
    }

//    @GetMapping
//    private Mono<MusicAsResource> findByArtistAndSongName(@RequestAttribute String artist,
//                                                          @RequestAttribute String songName) {
//        return musicResource.findByMusicAttributes(artist, songName);
//    }
}
