package com.dharbar.template.service.userService;

import com.dharbar.template.service.userService.dto.User;
import com.dharbar.template.service.userService.repo.UserRepository;
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
                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("User not found")));
    }

    public Mono<User> save(User user) {
        final UserEntity userEntity = toEntity(user);
        return userRepository.save(userEntity)
                .map(this::toDto);
    }

    private User toDto(UserEntity userEntity){
        return User.builder()
                .username(userEntity.getUsername())
                .itunesUrl(userEntity.getItunesUrl())
                .build();
    }

    private UserEntity toEntity(User user){
        return UserEntity.builder()
                .id(ThreadLocalRandom.current().nextInt())
                .username(user.getUsername())
                .itunesUrl(user.getItunesUrl())
                .build();
    }
}
