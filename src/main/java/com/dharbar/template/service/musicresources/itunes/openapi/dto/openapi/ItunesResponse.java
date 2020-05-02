package com.dharbar.template.service.musicresources.itunes.openapi.dto.openapi;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class ItunesResponse {

    private Integer resultCount;

    private List<ItunesResult> results;
}
