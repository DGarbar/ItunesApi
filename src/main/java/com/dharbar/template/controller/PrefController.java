package com.dharbar.template.controller;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import com.dharbar.template.service.melodypref.service.MelodyPreferencesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
@RequestMapping("/pref")
public class PrefController {

    private final MelodyPreferencesService melodyPreferencesService;

    @PostMapping
    private Flux<MelodyPref> findByMusicAttributes(@RequestBody MusicAttributes musicAttributes) {
        return melodyPreferencesService.findByMusicAttributes(musicAttributes);
    }
}
