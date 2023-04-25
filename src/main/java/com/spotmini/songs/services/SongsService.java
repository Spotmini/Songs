package com.spotmini.songs.services;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.spotmini.songs.data.SongModel;
import com.spotmini.songs.db.Song;
import com.spotmini.songs.repositories.ArtistsRepository;
import com.spotmini.songs.repositories.SongsRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SongsService {
    private final SongsRepository repository;
    private final ArtistsRepository artistsRepository;
    private Drive drive;

    public SongsService(SongsRepository repository, ArtistsRepository artistsRepository) {
        this.repository = repository;
        this.artistsRepository = artistsRepository;
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .getApplicationDefault()
                    .createScoped(List.of(DriveScopes.DRIVE_FILE));
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
            drive = new Drive.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), requestInitializer)
                    .setApplicationName("Spotmini")
                    .build();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void addSong(SongModel song) {
        var songArtist = artistsRepository.findById(song.getArtist());
        if (songArtist.isEmpty()) return;
        var artist = songArtist.get();
        if (artist.getIsDeleted()) return;
        var fileId = uploadFile(song.getArtist() + " - " + song.getName(), song.getContent());
        repository.save(new Song(song.getArtist(), song.getName(), fileId));
    }

    public InputStream getSong(String artist, String name) {
        var record = repository.findById(Pair.of(artist, name));
        if (record.isEmpty()) return null;
        var fileId = record.get().getFileId();
        InputStream song = null;
        try {
            song = drive.files().get(fileId).executeMediaAsInputStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return song;
    }

    public boolean doesExist(String artist, String name) {
        return repository.findById(Pair.of(artist, name)).isPresent();
    }

    private String uploadFile(String filename, byte[] content) {
        try {
             File fileMetadata = new File();
             fileMetadata.setName(filename);
             var outputFile = new java.io.File("temp.mp3");
             try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                 outputStream.write(content);
             }
             FileContent mediaContent = new FileContent("audio/mp3", outputFile);
             File file = drive.files()
                     .create(fileMetadata, mediaContent)
                     .setFields("id")
                     .execute();
             return file.getId();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void deleteSong(String artist, String songName) {
        var songOption = repository.findById(Pair.of(artist, songName));
        if (songOption.isEmpty()) return;
        var song = songOption.get();
        try {
            drive.files().delete(song.getFileId());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        repository.delete(song);
    }

    public void deleteArtist(String artist) {
        for (var song : repository.findAllByArtist(artist)) {
            try {
                drive.files().delete(song.getFileId());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        repository.deleteAllByArtist(artist);
        artistsRepository.findByName(artist).setIsDeleted(true);
    }
}
