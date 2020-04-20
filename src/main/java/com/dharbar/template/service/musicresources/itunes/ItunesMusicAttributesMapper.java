package com.dharbar.template.service.musicresources.itunes;

import com.dharbar.template.controller.dto.MusicAttributes;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ItunesMusicAttributesMapper {

    public List<NameValuePair> mapMusicAttributes(MusicAttributes musicAttributes) {
        final List<NameValuePair> nameValuePairs = new ArrayList<>();

        prepareTerm(musicAttributes).ifPresent(nameValuePairs::add);

        nameValuePairs.add(new BasicNameValuePair("entity", "song"));
        nameValuePairs.add(new BasicNameValuePair("media", "music"));
        nameValuePairs.add(new BasicNameValuePair("explicit", "no"));
        return nameValuePairs;
    }

    public List<NameValuePair> mapMelody(String artist, String songName) {
        final String term = StringUtils.trimToEmpty(String.format("%s %s", artist, songName));
        return StringUtils.isBlank(term)
                ? Collections.emptyList()
                : Collections.singletonList(new BasicNameValuePair("term", term));
    }

    public List<NameValuePair> mapArtist(String artist) {
        return Collections.singletonList(new BasicNameValuePair("term", artist));
    }

    private Optional<BasicNameValuePair> prepareTerm(MusicAttributes musicAttributes) {
        final List<String> artists = musicAttributes.getArtists();
        if (CollectionUtils.isEmpty(artists)) {
            return Optional.empty();
        }

        // TODO parse multiple and create req
        return artists.stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .map(artist -> new BasicNameValuePair("term", artist))
                .findFirst();
    }

    // TODO Attribute
    // TODO Attribute
}
