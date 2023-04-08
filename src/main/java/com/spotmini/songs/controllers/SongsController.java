package com.spotmini.songs.controllers;

import com.spotmini.songs.data.SongModel;
import com.spotmini.songs.repositories.ArtistsRepository;
import com.spotmini.songs.services.SongsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/songs")
public class SongsController {
    private final SongsService service;

    public SongsController(SongsService service, ArtistsRepository artistsRepository) {
        this.service = service;
    }

    @PostMapping
    private void addSong(@RequestBody SongModel song) {
        service.addSong(song);
    }
}
