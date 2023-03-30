package com.spotmini.songs.repositories;

import com.spotmini.songs.db.Artists;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ArtistsRepository extends CassandraRepository<Artists, String> {
}
