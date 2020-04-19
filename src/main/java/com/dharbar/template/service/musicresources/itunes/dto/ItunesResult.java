package com.dharbar.template.service.musicresources.itunes.dto;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class ItunesResult {

    private Float artistId;
    private String artistName;
    private Float trackId;
    private String trackName;
    // playlist
    private String trackViewUrl;
    // Real m4a
    private String previewUrl;
    private Float trackTimeMillis;
    private String primaryGenreName;

    private String wrapperType;
    private String kind;
    private Float collectionId;
    private String collectionName;
    private String artistViewUrl;
    private String collectionViewUrl;
    private String artworkUrl30;
    private String artworkUrl60;
    private String artworkUrl100;
    private Float collectionPrice;
    private Float trackPrice;
    private String releaseDate;
    private String collectionExplicitness;
    private String trackExplicitness;
    private String country;
    private String currency;
    private Boolean isStreamable;
}
