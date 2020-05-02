package com.dharbar.template.service.melodypref.repo;

import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Profile("mongo")
public interface MelodyPreferenceRepo extends ReactiveMongoRepository<MelodyPref, String> {

    Flux<MelodyPref> findAllByArtist(String artist);

    Mono<MelodyPref> getByArtistAndSongName(String artist, String songName);

    Flux<MelodyPref> findByTagsContains(List<String> tags);
}
