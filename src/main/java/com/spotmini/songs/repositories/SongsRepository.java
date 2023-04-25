package com.spotmini.songs.repositories;

import com.spotmini.songs.db.Song;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.util.Pair;

import java.util.List;

public interface SongsRepository extends CassandraRepository<Song, Pair<String, String>> {
    List<Song> findAllByArtist(String artist);
    void deleteAllByArtist(String artist);
}
