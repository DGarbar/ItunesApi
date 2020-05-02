package com.dharbar.template.service.musicresources.itunes.devapi;

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
public class DevApiItunesMusicAttributesMapper {

    public List<NameValuePair> mapMusicAttributes(MusicAttributes musicAttributes) {
        final List<NameValuePair> nameValuePairs = getDefault();
        prepareTerm(musicAttributes).ifPresent(nameValuePairs::add);

        return nameValuePairs;
    }

    private List<NameValuePair> getDefault() {
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("types", "songs"));
        return nameValuePairs;
    }

    private List<NameValuePair> getDefault(NameValuePair nameValuePair) {
        List<NameValuePair> nameValuePairs = getDefault();
        nameValuePairs.add(nameValuePair);
        return nameValuePairs;
    }

    public List<NameValuePair> mapMelody(String artist, String songName) {
        final String term = StringUtils.trimToEmpty(String.format("%s %s", artist, songName));
        return StringUtils.isBlank(term)
                ? Collections.emptyList()
                : getDefault(new BasicNameValuePair("term", term));
    }

    public List<NameValuePair> mapArtist(String artist) {
        return getDefault(new BasicNameValuePair("term", artist));
    }

    private Optional<BasicNameValuePair> prepareTerm(MusicAttributes musicAttributes) {
        var artists = musicAttributes.getArtists();
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
}
