package com.dharbar.template.service.musicresources.itunes.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@AllArgsConstructor
@Value
public class ItunesResponse {

    private Integer resultCount;

    private List<ItunesResult> results;
}
