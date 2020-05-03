package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists;

import lombok.Value;

@Value(staticConstructor = "of")
public class ItunesArtistsData {

    String id;
    String href;
    ItunesArtistsAttributes attributes;
    String type;
}
