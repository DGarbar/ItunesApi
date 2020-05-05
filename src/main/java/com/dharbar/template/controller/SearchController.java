package com.dharbar.template.controller;

import com.dharbar.template.service.musicresources.MusicSearcher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {

    private final MusicSearcher musicSearcher;

    @GetMapping
    private Mono<List<String>> findByArtist(@RequestParam String artist) {
        return musicSearcher.findArtist(artist).collectList();
    }
}

