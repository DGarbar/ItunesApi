package com.dharbar.template.service.melodypref.service;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.melodypref.repo.MelodyPreferenceRepo;
import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Profile({"mongo", "mongoprod"})
@AllArgsConstructor
@Service
public class MusicAttributesPreferencesService {

    private ReactiveMongoTemplate reactiveMongoTemplate;
    private MelodyPreferenceRepo melodyPreferenceRepo;


    public Mono<MelodyPref> updateSongs(MelodyPref melodyPref) {
        return melodyPreferenceRepo.getFirstByArtistAndSongName(melodyPref.getArtist(), melodyPref.getSongName())
                .defaultIfEmpty(melodyPref)
                .flatMap(melodyPref1 -> {
                    MelodyPref updated = melodyPref1.toBuilder()
                            .genres(melodyPref.getGenres())
                            .tags(melodyPref.getTags())
                            .build();
                    return melodyPreferenceRepo.save(updated);
                });
    }

    public Flux<MelodyPref> findByMusicAttributes(MusicAttributes musicAttributes) {
        return allCriteriaDefinitionQuery(musicAttributes)
                .map(query -> reactiveMongoTemplate.find(query, MelodyPref.class))
                .orElse(Flux.empty());
    }

    private Optional<Query> allCriteriaDefinitionQuery(MusicAttributes musicAttributes) {
        return Stream.of(
                criteriaByArtist(musicAttributes.getArtists()),
                criteriaByGenres(musicAttributes.getGenres()),
                criteriaByTags(musicAttributes.getTags()))
                .flatMap(Optional::stream)
                .reduce(Criteria::andOperator)
                .map(Query::new);
    }

    private Optional<Criteria> criteriaByArtist(List<String> artists) {
        return CollectionUtils.isNotEmpty(artists)
                ? Optional.of(Criteria.where("artist").in(artists))
                : Optional.empty();
    }

    private Optional<Criteria> criteriaByGenres(List<String> genres) {
        return CollectionUtils.isNotEmpty(genres)
                ? Optional.of(Criteria.where("genre").in(genres))
                : Optional.empty();
    }

    private Optional<Criteria> criteriaByTags(List<String> tags) {
        return CollectionUtils.isNotEmpty(tags)
                // Can replace with "all" to full eq
                ? Optional.of(Criteria.where("tags").in(tags))
                : Optional.empty();
    }
}
