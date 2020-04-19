package com.dharbar.template.service.userService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Value
public class User {

    @NotNull
    String username;

    @NotNull
    @JsonProperty("itunes-url")
    String itunesUrl;
}
