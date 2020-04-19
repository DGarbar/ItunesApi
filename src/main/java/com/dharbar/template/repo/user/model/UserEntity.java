package com.dharbar.template.repo.user.model;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class UserEntity {

    Integer id;

    String username;

    String itunesUrl;
}
