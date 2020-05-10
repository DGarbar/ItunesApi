package com.dharbar.template.service.userService.repo;

import com.dharbar.template.service.userService.repo.entity.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Profile("local")
@Repository
public class LocalUserRepository implements UserRepository {

    private Map<Integer, UserEntity> idToUserEntity;

    public LocalUserRepository() {
        this.idToUserEntity = new HashMap<>();
        UserEntity testEntity = UserEntity.of(1, "test", "http://test");
        idToUserEntity.put(1, testEntity);
    }

    public Mono<UserEntity> getById(Integer id) {
        return Mono.justOrEmpty(idToUserEntity.get(id));
    }

    public Mono<UserEntity> save(UserEntity userEntity) {
        idToUserEntity.put(userEntity.getId(), userEntity);
        return Mono.just(userEntity);
    }
}
