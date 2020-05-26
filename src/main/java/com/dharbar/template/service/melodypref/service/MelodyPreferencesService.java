package com.dharbar.template.service.melodypref.service;

import com.dharbar.template.service.melodypref.repo.MelodyPreferenceRepo;
import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Profile({"mongo", "mongoprod"})
@AllArgsConstructor
@Service
public class MelodyPreferencesService {

    private ReactiveMongoTemplate reactiveMongoTemplate;
    private MelodyPreferenceRepo melodyPreferenceRepo;

    public Mono<MelodyPref> findByArtistAndSongNames(String artist, String songName) {
//        Criteria byArtist = Criteria.where("artist").alike(Example.of(artist));
//        Criteria bySongs = Criteria.where("songName").in(songName);
//        Criteria criteria = byArtist.andOperator(bySongs);
//
//        return reactiveMongoTemplate.findOne(new Query(criteria), MelodyPref.class)
//                .doOnError(throwable -> log.error(throwable.getMessage(), throwable))
//                .onErrorReturn(MelodyPref.builder().artist(artist).songName(songName).build())
//                .defaultIfEmpty(MelodyPref.builder().artist(artist).songName(songName).build());
//

        return melodyPreferenceRepo.getFirstByArtistAndSongName(artist, songName)
                .doOnError(throwable -> log.error(throwable.getMessage(), throwable))
                .onErrorReturn(MelodyPref.builder().artist(artist).songName(songName).build())
                .defaultIfEmpty(MelodyPref.builder().artist(artist).songName(songName).build());
    }
//
//    public Flux<MelodyPref> findByArtistAndSongNames(List<ArtistAndSong> artistAndSongName) {
//        Map<String, List<String>> artistToSongName = artistAndSongName.stream()
//                .collect(Collectors.groupingBy(ArtistAndSong::getArtist,
//                        Collectors.mapping(ArtistAndSong::getSongName, Collectors.toList())));
//
//        return createQueryFindArtistsAndSongs(artistToSongName)
//                .map(query -> reactiveMongoTemplate.find(query, MelodyPref.class))
//                .map(melodyPrefFlux -> findAndAddNotExisting(melodyPrefFlux, artistAndSongName))
//                .orElse(Flux.empty());
//    }
//
//    // TODO make something more interesting
//    private Flux<MelodyPref> findAndAddNotExisting(Flux<MelodyPref> foundMelodies,
//                                                   List<ArtistAndSong> artistAndSongName) {
//        List<ArtistAndSong> notExistingArtistAndSongName = new ArrayList<>(artistAndSongName);
//        return foundMelodies
//                .doOnNext(melodyPref -> notExistingArtistAndSongName.removeIf(
//                        m -> melodyPref.getArtist().equals(m.getArtist())
//                                && melodyPref.getSongName().equals(m.getSongName())))
//
//                .thenMany(Flux.fromIterable(emptyTags(notExistingArtistAndSongName)));
//    }
//
//    private List<MelodyPref> emptyTags(List<ArtistAndSong> notExistingArtistAndSongName) {
//        return notExistingArtistAndSongName.stream()
//                .map(artistAndSong -> MelodyPref.builder()
//                        .artist(artistAndSong.getArtist())
//                        .songName(artistAndSong.getSongName())
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    private Optional<Query> createQueryFindArtistsAndSongs(Map<String, List<String>> artistToSongName) {
//        return artistToSongName.entrySet().stream()
//                .map(artistAndSongName -> criteriaByArtist(artistAndSongName.getKey(), artistAndSongName.getValue()))
//                .reduce(Criteria::orOperator)
//                .map(Query::new);
//    }
//
//    private Criteria criteriaByArtist(String artist, List<String> songs) {
//        Criteria byArtist = Criteria.where("artist").alike(Example.of(artist));
//        Criteria bySongs = Criteria.where("songName").in(songs);
//        return byArtist.andOperator(bySongs);
//
//    }
}
