package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ItunesSongsAttributesArtwork {

    Integer width;
    Integer height;
    String url;
    String bgColor;
    String textColor1;
    String textColor2;
    String textColor3;
    String textColor4;
}
