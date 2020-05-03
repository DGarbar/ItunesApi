package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi;

import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.artists.ItunesArtists;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs.ItunesSongs;
import lombok.Value;

@Value(staticConstructor = "of")
public class ItunesResult {

    ItunesSongs songs;

    ItunesArtists artists;
}
