package com.dharbar.template.service.musicresources.dto;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class MusicAsResource {

    private String artist;
    private String songName;
    private String fileUrl;
    private Float trackTimeMillis;
    private String genre;
}
