package com.dharbar.template.service.melodypref.service.it;

import com.dharbar.template.service.melodypref.repo.MelodyPreferenceRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MelodyPreferencesServiceTest extends MongoIntegrationTest {

    @Autowired
    private MelodyPreferenceRepo melodyPreferenceRepo;

    @Test
    void name() {
        melodyPreferenceRepo.findAll();
    }
}

