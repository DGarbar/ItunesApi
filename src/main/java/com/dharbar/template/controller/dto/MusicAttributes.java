package com.dharbar.template.controller.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class MusicAttributes {

    private List<String> artists;
    private List<String> genres;
    private List<String> tags;
}

