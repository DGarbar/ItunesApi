package com.dharbar.template.service.userService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value(staticConstructor = "of")
public class User {

    @NotNull
    String username;

    @NotNull
    @JsonProperty("itunes-url")
    String itunesUrl;
}
