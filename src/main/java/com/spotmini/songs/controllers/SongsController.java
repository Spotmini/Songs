package com.spotmini.songs.controllers;

import com.spotmini.songs.data.SongModel;
import com.spotmini.songs.services.SongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.HashMap;

@RestController
@RequestMapping("/songs")
public class SongsController {
    private final SongsService service;

    @Autowired
    public SongsController(SongsService service) {
        this.service = service;
    }

    @PostMapping
    private void addSong(@RequestBody SongModel song) {
        service.addSong(song);
    }

    @KafkaListener(groupId = "songs", topics = "test-topic")
    private void deleteSong(HashMap<String, String> data) {
        var artist = data.get("artistName");
        var song = data.get("songName");

        if (song != null) {
            service.deleteSong(artist, song);
            return;
        }
        service.deleteArtist(artist);
    }

    @GetMapping
    private boolean songExists(String artist, String song) {
        return service.doesExist(artist, song);
    }

    @GetMapping
    private InputStream getSong(String artist, String song) {
        return service.getSong(artist, song);
    }
}
