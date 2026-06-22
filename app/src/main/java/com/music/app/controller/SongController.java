package com.music.app.controller;

import com.music.app.model.Song;
import com.music.app.service.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public List<Song> findAll() {
        return songService.findAll();
    }

    @GetMapping("/{id}")
    public Song findById(@PathVariable Long id) {
        return songService.findById(id);
    }

    @PostMapping
    public Song create(@RequestBody Song song) {
        return songService.save(song);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        songService.delete(id);
    }
}

