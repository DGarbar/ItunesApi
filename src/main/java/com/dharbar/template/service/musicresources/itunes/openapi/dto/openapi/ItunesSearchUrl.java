package com.dharbar.template.service.musicresources.itunes.openapi.dto.openapi;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Value(staticConstructor = "of")
//@ConfigurationProperties(prefix = "itunes.search.url")
//@Component
public class ItunesSearchUrl {

    @NotNull
    private String host;

    @Pattern(regexp = "musicArtist|musicTrack|album|musicVideo|mix|song")
    private String entity;

    @Pattern(regexp = "mixTerm|genreIndex|artistTerm|composerTerm|albumTerm|ratingIndex|songTerm")
    private String attribute;

    @Positive
    @Max(20)
    private int limit;

    @Pattern(regexp = "yes|no")
    private String explicit;
}

