package com.dharbar.template.service.musicresources.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class MusicAsResource {

    private String artist;
    private String songName;
    private String fileUrl;
    private Long trackTimeMillis;
    private List<String> genres;
}
