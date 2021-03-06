package com.dharbar.template.service.userService.repo;

import com.dharbar.template.service.userService.repo.entity.UserEntity;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<UserEntity> getById(Integer id);

    Mono<UserEntity> save(UserEntity userEntity);
}
