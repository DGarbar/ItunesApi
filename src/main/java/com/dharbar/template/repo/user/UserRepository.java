package com.dharbar.template.repo.user;

import com.dharbar.template.repo.user.model.UserEntity;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<UserEntity> getById(Integer id);

    Mono<UserEntity> save(UserEntity userEntity);
}
