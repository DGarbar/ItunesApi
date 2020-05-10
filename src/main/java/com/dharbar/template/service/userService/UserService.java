package com.dharbar.template.service.userService;

import com.dharbar.template.service.userService.dto.User;
import com.dharbar.template.service.userService.repo.UserRepository;
import com.dharbar.template.service.userService.repo.exception.UserNotFoundException;
import com.dharbar.template.service.userService.repo.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> getById(Integer id) {
        return userRepository.getById(id)
                .map(this::toDto)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException("User not found")));
    }

    public Mono<User> save(User user) {
        final UserEntity userEntity = toEntity(user);
        return userRepository.save(userEntity)
                .map(this::toDto);
    }

    private User toDto(UserEntity userEntity) {
        return User.of(userEntity.getUsername(), userEntity.getItunesUrl());
    }

    private UserEntity toEntity(User user) {
        int id = ThreadLocalRandom.current().nextInt();
        return UserEntity.of(id, user.getUsername(), user.getItunesUrl());
    }
}
