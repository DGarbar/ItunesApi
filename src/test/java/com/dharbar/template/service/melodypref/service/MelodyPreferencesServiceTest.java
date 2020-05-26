package com.dharbar.template.service.melodypref.service;

import com.dharbar.template.controller.dto.MusicAttributes;
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

import java.util.List;


@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@ActiveProfiles("mongo")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataMongoTest
@Import(MusicAttributesPreferencesService.class)
@ExtendWith(SpringExtension.class)
class MelodyPreferencesServiceTest {

    @Autowired
    private MelodyPreferencesService target;

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

//        StepVerifier.create(target.findByArtistAndSongNames(Map.of("test", List.of("testSong"))))
//                .expectNextCount(0)
//                .verifyComplete();
    }

}
