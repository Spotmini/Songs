package com.spotmini.songs.controllers;

import com.spotmini.songs.data.ArtistModel;
import com.spotmini.songs.db.Artist;
import com.spotmini.songs.repositories.ArtistsRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
public class ArtistsController {
    private final ArtistsRepository repository;

    public ArtistsController(ArtistsRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public void addArtist(@RequestBody ArtistModel artist) {
        if (repository.findById(artist.getName()).isPresent()) {
            return;
        }

        repository.save(new Artist(artist.getName()));
    }
}
