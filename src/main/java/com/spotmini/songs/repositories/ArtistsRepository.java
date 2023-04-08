package com.spotmini.songs.repositories;

import com.spotmini.songs.db.Artist;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ArtistsRepository extends CassandraRepository<Artist, String> {
}
