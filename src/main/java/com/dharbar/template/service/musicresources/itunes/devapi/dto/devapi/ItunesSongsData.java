package com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ItunesSongsData {
    public String id;
    public String type;
    public String href;
    public ItunesSongsAttributes attributes;
}
