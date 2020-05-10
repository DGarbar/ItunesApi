package com.dharbar.template.service.userService.repo.entity;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserEntity {

    Integer id;

    String username;

    String itunesUrl;
}
