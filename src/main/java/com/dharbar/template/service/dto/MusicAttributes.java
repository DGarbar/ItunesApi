package com.dharbar.template.service.dto;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class MusicAttributes {

    private String author;
    private String song;

    private Boolean bigAttitude;
    private Boolean aThink;
    private Boolean aSad;
    private Boolean aHate;
    private Boolean canSleep;
    private Boolean swing;
    private Boolean move;
    private Boolean classic;
}

