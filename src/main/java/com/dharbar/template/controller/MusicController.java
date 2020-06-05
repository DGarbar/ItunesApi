package com.dharbar.template.controller;

import com.dharbar.template.service.melodypref.service.MelodyPreferencesService;
import com.dharbar.template.service.musicresources.SongResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@RestController
@RequestMapping("/music")
public class MusicController {

    private final SongResource songResource;
    private final MelodyPreferencesService melodyPreferencesService;

//    @PostMapping
//    private Flux<MusicAsResource> findByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
//        return songResource.findByMusicAttributes(musicAttributes);
//    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    private Flux<MusicAsResource> findBy(@NotBlank @RequestParam String search) {
        return songResource.findBy(search);
    }

//    // TODO refactor
//    @GetMapping("/pref")
//    private Flux<MusicAsResource> findByAndPref(@NotBlank @RequestParam String search) {
//        return songResource.findBy(search)
//                .log()
//                .flatMap(musicAsResource -> melodyPreferencesService.findByArtistAndSongNames(
//                        musicAsResource.getArtist(), musicAsResource.getSongName())
//                        .log()
//                        .map(melodyPref -> musicAsResource.toBuilder()
//                                .tags(melodyPref.getTags())
//                                .genres(List.of("test"))
//                                .build()));
//    }

//    @GetMapping("/test")
//    private Flux<String> findByAndPref() {
//        return Flux.just("test2", "test3", "test4", "test5")
//                .log()
//                .flatMap(s -> melodyPreferencesService.findByArtistAndSongNames(s, s)
//                        .log()
//                        .map(MelodyPref::toString));
//    }

//    @GetMapping("/artists")
//    private Mono<MusicAsResource> findByArtistAndSongName(@RequestParam String name,
//                                                          @RequestParam String songName) {
//        return songResource.findMelody(name, songName);
//    }
}
