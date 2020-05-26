package com.dharbar.template.service.melodypref.service.it;

import com.dharbar.template.controller.dto.MusicAttributes;
import com.dharbar.template.service.melodypref.repo.entity.MelodyPref;
import com.dharbar.template.service.melodypref.service.MusicAttributesPreferencesService;
import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.List;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@ActiveProfiles("mongo")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataMongoTest
@Import(MusicAttributesPreferencesService.class)
@ExtendWith(SpringExtension.class)
class MusicAttributesPreferencesServiceTest {

    @Autowired
    private MusicAttributesPreferencesService target;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Or recreate MongoBy annotation
    @AfterEach
    void clean() {
        // TODO
//        mongoTemplate.getDb().drop();
    }

    @Test
    void findByMusicAttributes_returnEmpty_noResult() {
        // when and then
        var request = MusicAttributes.builder()
                .artists(List.of("test"))
                .build();

        StepVerifier.create(target.findByMusicAttributes(request))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findByMusicAttributes_returnMelodyPref_foundByArtist() {
        // given
        var melodyPref1 = MelodyPref.builder().artist("test").genre("testGenre1").build();
        var melodyPref2 = MelodyPref.builder().artist("test").genre("testGenre2").build();
        mongoTemplate.save(melodyPref1, "melody");
        mongoTemplate.save(melodyPref2, "melody");

        // when and then
        var request = MusicAttributes.builder()
                .artists(List.of("test"))
                .build();

        StepVerifier.create(target.findByMusicAttributes(request))
                .expectNext(melodyPref1, melodyPref2)
                .verifyComplete();
    }

    @Test
    void findByMusicAttributes_returnMelodyPref_foundByGenre() {
        // given
        var melodyPref1 = MelodyPref.builder().artist("test").genre("testGenre1").build();
        var melodyPref2 = MelodyPref.builder().artist("test").genre("testGenre2").build();
        mongoTemplate.save(melodyPref1, "melody");
        mongoTemplate.save(melodyPref2, "melody");

        // when and then
        var request = MusicAttributes.builder()
                .genres(List.of("testGenre1"))
                .build();

        StepVerifier.create(target.findByMusicAttributes(request))
                .expectNext(melodyPref1)
                .verifyComplete();
    }

    @Test
    void findByMusicAttributes_returnMelodyPref_foundByTags() {
        // given
        var melodyPref1 = MelodyPref.builder().artist("test").tags(List.of("tag1", "tag2")).build();
        var melodyPref2 = MelodyPref.builder().artist("test").tags(List.of("tag2")).build();
        mongoTemplate.save(melodyPref1, "melody");
        mongoTemplate.save(melodyPref2, "melody");

        // when and then
        var request = MusicAttributes.builder()
                .tags(List.of("tag2"))
                .build();

        StepVerifier.create(target.findByMusicAttributes(request))
                .expectNext(melodyPref1, melodyPref2)
                .verifyComplete();
    }

    @Test
    void findByMusicAttributes_returnMelodyPref_foundByTagsAndGenres() {
        // given
        var melodyPref1 = MelodyPref.builder().artist("test").tags(List.of("tag1", "tag2")).build();
        var melodyPref2 = MelodyPref.builder().artist("test1").tags(List.of("tag2")).genre("genre1").build();
        var melodyPref3 = MelodyPref.builder().artist("test2").tags(List.of("tag2")).genre("genre1").build();
        mongoTemplate.save(melodyPref1, "melody");
        mongoTemplate.save(melodyPref2, "melody");
        mongoTemplate.save(melodyPref3, "melody");

        // when and then
        var request = MusicAttributes.builder()
                .tags(List.of("tag2"))
                .genres(List.of("genre1"))
                .build();

        StepVerifier.create(target.findByMusicAttributes(request))
                .expectNext(melodyPref2, melodyPref3)
                .verifyComplete();
    }

    @Test
    void findByMusicAttributes_returnMelodyPref_foundByMultipleTagsAndGenres() {
        // given
        var melodyPref1 = MelodyPref.builder().artist("test").tags(List.of("tag1")).genre("genre1").build();
        var melodyPref2 = MelodyPref.builder().artist("test1").tags(List.of("tag1", "tag2", "tag3"))
                .genre("genre1").build();
        var melodyPref3 = MelodyPref.builder().artist("test2").tags(List.of("tag2")).genre("genre1").build();
        mongoTemplate.save(melodyPref1, "melody");
        mongoTemplate.save(melodyPref2, "melody");
        mongoTemplate.save(melodyPref3, "melody");

        // when and then
        var request = MusicAttributes.builder()
                .tags(List.of("tag1", "tag2"))
                .genres(List.of("genre1"))
                .build();

        StepVerifier.create(target.findByMusicAttributes(request))
                .expectNext(melodyPref1, melodyPref2, melodyPref3)
                .verifyComplete();
    }
}

