package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
class DevApiItunesMusicAttributesMapperTest {

    private DevApiItunesMusicAttributesMapper target;

    @BeforeEach
    void setUp() {
        target = new DevApiItunesMusicAttributesMapper();
    }

    @Test
    void mapSong_createParam_songIsProvided() {
        // when
        var result = target.mapSong("testSongName");

        // then
        assertThat(result).containsOnly(
                new BasicNameValuePair("types", "songs"),
                new BasicNameValuePair("term", "testSongName"));
    }

    @Test
    void mapSong_createWithEmpty_songIsEmpty() {
        // when
        var result = target.mapSong("");

        // then
        assertThat(result).containsOnly(
                new BasicNameValuePair("types", "songs"),
                new BasicNameValuePair("term", ""));
    }

    @Test
    void mapArtist_createParam_artistIsProvided() {
        // when
        var result = target.mapArtist("testArtistName");

        // then
        assertThat(result).containsOnly(
                new BasicNameValuePair("types", "artists"),
                new BasicNameValuePair("term", "testArtistName"));
    }

    @Test
    void mapArtist_createWithEmpty_artistIsEmpty() {
        // when
        var result = target.mapArtist("");

        // then
        assertThat(result).containsOnly(
                new BasicNameValuePair("types", "artists"),
                new BasicNameValuePair("term", ""));
    }
}
