package com.music.app.service;

import com.music.app.model.Song;
import com.music.app.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> findAll() {
        return songRepository.findAll();
    }

    public Song findById(Long id) {
        return songRepository.findById(id).orElseThrow(() -> new RuntimeException("Canción no encontrada"));
    }

    public Song save(Song song) {
        return songRepository.save(song);
    }

    public void delete(Long id) {
        songRepository.deleteById(id);
    }
}
