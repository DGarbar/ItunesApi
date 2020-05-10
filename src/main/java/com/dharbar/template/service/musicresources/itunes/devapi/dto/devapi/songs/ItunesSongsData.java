package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs;

import lombok.Value;

@Value(staticConstructor = "of")
public class ItunesSongsData {
    String id;
    String type;
    String href;
    ItunesSongsAttributes attributes;
}
