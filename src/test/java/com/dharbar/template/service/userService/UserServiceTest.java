package com.dharbar.template.service.userService;

import com.dharbar.template.service.userService.dto.User;
import com.dharbar.template.service.userService.repo.UserRepository;
import com.dharbar.template.service.userService.repo.entity.UserEntity;
import com.dharbar.template.service.userService.repo.exception.UserNotFoundException;
import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService target;

    @Test
    void getById_returnUser_userIsFound() {
        // given
        given(repository.getById(any())).willReturn(Mono.just(UserEntity.of(1, "name", "url")));

        // when and then
        StepVerifier.create(target.getById(1))
                .expectNext(User.of("name", "url"))
                .verifyComplete();

        verify(repository).getById(1);
    }

    @Test
    void getById_exception_userIsNotFound() {
        // given
        given(repository.getById(any())).willReturn(Mono.empty());

        // when and then
        StepVerifier.create(target.getById(1))
                .verifyError(UserNotFoundException.class);

        verify(repository).getById(1);
    }

    @Test
    void save_saveUserAndReturn_repositoryIsSaved() {
        // given
        given(repository.save(any())).willReturn(Mono.just(UserEntity.of(1, "name", "url")));

        // when
        StepVerifier.create(target.save(User.of("name", "url")))
                .expectNext(User.of("name", "url"))
                .verifyComplete();

        verify(repository).save(any());
    }
}
