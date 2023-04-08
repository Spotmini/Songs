package com.spotmini.songs.db;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Song {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String artist;
    @PrimaryKeyColumn
    private String name;
    private String fileId;

    public Song(String artist, String name, String fileId) {
        this.artist = artist;
        this.name = name;
        this.fileId = fileId;
    }
}
