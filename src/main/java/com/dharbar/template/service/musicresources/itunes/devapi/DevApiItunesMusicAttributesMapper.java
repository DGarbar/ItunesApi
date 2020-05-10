package com.dharbar.template.service.musicresources.itunes.devapi;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevApiItunesMusicAttributesMapper {

    private static final BasicNameValuePair SONG_TYPE = new BasicNameValuePair("types", "songs");
    private static final BasicNameValuePair ARTISTS_TYPE = new BasicNameValuePair("types", "artists");

//    // TODO
//    public List<NameValuePair> mapSong(MusicAttributes musicAttributes) {
//        prepareTerm(musicAttributes).ifPresent(nameValuePairs::add);
//
//        return null;
//    }

    public List<NameValuePair> mapSong(String term) {
        return List.of(SONG_TYPE, new BasicNameValuePair("term", term));
    }

    public List<NameValuePair> mapArtist(String term) {
        return List.of(ARTISTS_TYPE, new BasicNameValuePair("term", term));
    }

//DevApiItunesRequestSearcher
//    private Optional<BasicNameValuePair> prepareTerm(MusicAttributes musicAttributes) {
//        var artists = musicAttributes.getArtists();
//        if (CollectionUtils.isEmpty(artists)) {
//            return Optional.empty();
//        }
//
//        // TODO parse multiple and create req
//        return artists.stream()
//                .map(StringUtils::trimToEmpty)
//                .filter(StringUtils::isNotBlank)
//                .map(artist -> new BasicNameValuePair("term", artist))
//                .findFirst();
//    }

    // TODO Attribute
}
