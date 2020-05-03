package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class ItunesArtists {

    String href;
//    String next;
    List<ItunesArtistsData> data;

}
