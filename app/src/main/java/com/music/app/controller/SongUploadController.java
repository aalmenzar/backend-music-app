package com.music.app.controller;

import com.mpatric.mp3agic.Mp3File;
import com.music.app.model.Song;
import com.music.app.service.SongService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/songs")
public class SongUploadController {


    @Value("${audio.storage.path}")
    private String storagePath;

    private final SongService songService;

    public SongUploadController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Song> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist) {

        validateAudioFile(file);
        String savedPath = saveFile(file);
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setFilePath(savedPath);
        song.setMimeType(file.getContentType());
        song.setDurationSeconds(extractDuration(Paths.get(savedPath), file.getContentType()));

        Song saved = songService.save(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    private Integer extractDuration(Path filePath, String contentType) {
        try {
            if (contentType != null && contentType.equals("audio/mpeg")) {
                Mp3File mp3 = new Mp3File(filePath.toFile());
                return (int) mp3.getLengthInSeconds();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }


    private void validateAudioFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo está vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Solo se permiten archivos de audio");
        }
    }

    private String saveFile(MultipartFile file) {
        try {
            Path storageDir = Paths.get(storagePath);
            Files.createDirectories(storageDir);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destination = storageDir.resolve(filename);
            file.transferTo(destination);
            return destination.toString();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo de audio");
        }
    }
}