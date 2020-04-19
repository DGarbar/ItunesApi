package com.dharbar.template.service.musicresources.itunes;

import com.dharbar.template.service.dto.MusicAttributes;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ItunesMusicAttributesMapper {

    public List<NameValuePair> mapMusicAttributes(MusicAttributes musicAttributes){
        final List<NameValuePair> nameValuePairs = new ArrayList<>();

        prepareTerm(musicAttributes).ifPresent(nameValuePairs::add);

        nameValuePairs.add(new BasicNameValuePair("entity", "song"));
        nameValuePairs.add(new BasicNameValuePair("media", "music"));
        nameValuePairs.add(new BasicNameValuePair("explicit", "no"));
        return nameValuePairs;
    }

    public List<NameValuePair> mapArtist(String artist){
        return Collections.singletonList(new BasicNameValuePair("term", artist));
    }

    private Optional<BasicNameValuePair> prepareTerm(MusicAttributes musicAttributes){
        final String author = StringUtils.trimToEmpty(musicAttributes.getAuthor());
        final String songName = StringUtils.trimToEmpty(musicAttributes.getSong());
        final String term = StringUtils.trimToEmpty(String.format("%s %s", author, songName));
        return StringUtils.isBlank(term) ? Optional.empty() : Optional.of(new BasicNameValuePair("term", term));
    }

    // TODO Attribute
    // TODO Attribute
}
