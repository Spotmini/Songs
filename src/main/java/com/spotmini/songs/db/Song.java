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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
