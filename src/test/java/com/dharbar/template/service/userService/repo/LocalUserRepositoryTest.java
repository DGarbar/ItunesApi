package com.dharbar.template.service.userService.repo;

import com.dharbar.template.service.userService.repo.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class LocalUserRepositoryTest {

    private LocalUserRepository target;

    @BeforeEach
    void setUp() {
        target = new LocalUserRepository();
    }

    @Test
    void getById_returnUser_userIsFound() {
        // when and then
        StepVerifier.create(target.getById(1))
                .expectNext(UserEntity.of(1, "test", "http://test"))
                .verifyComplete();
    }

    @Test
    void save_saveUserAndReturn_repositoryIsSaved() {
        // when
        StepVerifier.create(target.save(UserEntity.of(1, "name", "url")))
                .expectNext(UserEntity.of(1, "name", "url"))
                .verifyComplete();
    }
}
