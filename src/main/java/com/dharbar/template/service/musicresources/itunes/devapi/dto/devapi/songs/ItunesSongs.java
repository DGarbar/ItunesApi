package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.songs;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class ItunesSongs {

    String href;
    String next;
    List<ItunesSongsData> data;

}
