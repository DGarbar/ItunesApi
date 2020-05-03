package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class ItunesArtistsAttributes {

    String name;
    String url;
    List<String> genreNames;
//    JsonNode editorialNotes;
}
