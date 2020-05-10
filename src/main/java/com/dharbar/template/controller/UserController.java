package com.dharbar.template.controller;

import com.dharbar.template.service.userService.UserService;
import com.dharbar.template.service.userService.dto.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    private Mono<User> getByUserId(@Min(1) @PathVariable Integer userId) {
        return userService.getById(userId);
    }
}
