package com.dharbar.template.controller;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import com.dharbar.template.service.melodypref.service.MusicAttributesPreferencesService;
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

    @PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    private Flux<MelodyPref> findByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
        return musicAttributesPreferencesService.findByMusicAttributes(musicAttributes);
    }

    @PostMapping("/new")
    private Mono<MelodyPref> addNewPref(@RequestBody MelodyPref melodyPref) {
        return musicAttributesPreferencesService.updateSongs(melodyPref);
    }
}
