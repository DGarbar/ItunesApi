package com.dharbar.template.service.musicresources.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class MusicAsResource {

    String artist;
    String songName;
    String fileUrl;
    Long trackTimeMillis;
    List<String> genres;
    List<String> tags;
}
