package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs;

import lombok.Value;

@Value(staticConstructor = "of")
public class ItunesSongsAttributesPlayParams {
    String id;
    String kind;
}
