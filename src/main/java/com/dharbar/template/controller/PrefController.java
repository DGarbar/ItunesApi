package com.dharbar.template.controller;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import com.dharbar.template.service.melodypref.service.MusicAttributesPreferencesService;
import com.dharbar.template.service.musicresources.SongResource;
import com.dharbar.template.service.musicresources.dto.MusicAsResource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile({"mongo", "mongoprod"})
@AllArgsConstructor
@RestController
@RequestMapping("/pref")
public class PrefController {

    private final MusicAttributesPreferencesService musicAttributesPreferencesService;
    private final SongResource songResource;

    @PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    private Flux<MelodyPref> findByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
        return musicAttributesPreferencesService.findByMusicAttributes(musicAttributes);
    }


    @PostMapping("/music")
    private Flux<MusicAsResource> findMusicByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
        return musicAttributesPreferencesService.findByMusicAttributes(musicAttributes)
                .flatMap(dbPref -> songResource.findMelody(dbPref.getArtist(), dbPref.getSongName())
                        .map(musicRes -> MusicAsResource.builder()
                                .artist(musicRes.getArtist())
                                .songName(musicRes.getSongName())
                                .fileUrl(musicRes.getFileUrl())
                                .trackTimeMillis(musicRes.getTrackTimeMillis())
                                .genres(musicRes.getGenres())
                                .tags(dbPref.getTags())
                                .build()));
    }

    @PostMapping("/new")
    private Mono<MelodyPref> addNewPref(@RequestBody MelodyPref melodyPref) {
        return musicAttributesPreferencesService.updateSongs(melodyPref);
    }
}
