package com.spotmini.songs.repositories;

import com.spotmini.songs.db.Song;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.util.Pair;

public interface SongsRepository extends CassandraRepository<Song, Pair<String, String>> {
}
