package com.dharbar.template.service.melodypref.repo.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder(toBuilder = true)
@Data
@Document("melody")
@CompoundIndexes({
        @CompoundIndex(def = "{'artist':1, 'songName':-1}", name = "artist_song_name_index"),
})
public class MelodyPref {

    @Id
    private String id;

    private String artist;

    private String songName;

    private String genre;

    private List<String> tags;

}
