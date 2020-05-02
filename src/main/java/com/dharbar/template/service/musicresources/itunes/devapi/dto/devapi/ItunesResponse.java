package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi;

import lombok.Value;

@Value(staticConstructor = "of")
public class ItunesResponse {

    ItunesResult results;
}
