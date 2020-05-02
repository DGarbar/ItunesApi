package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class ItunesSongsAttributes {

    List<ItunesSongsAttributesPreview> previews;
    ItunesSongsAttributesArtwork artwork;
    String artistName;
    String url;
    Integer discNumber;
    List<String> genreNames;
    Long durationInMillis;
    String releaseDate;
    String name;
    String isrc;
    Boolean hasLyrics;
    String albumName;
    ItunesSongsAttributesPlayParams playParams;
    Integer trackNumber;
    String composerName;
}
