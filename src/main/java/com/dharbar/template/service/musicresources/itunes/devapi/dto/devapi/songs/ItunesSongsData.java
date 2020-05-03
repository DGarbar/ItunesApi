package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ItunesSongsData {
    String id;
    String type;
    String href;
    ItunesSongsAttributes attributes;
}
