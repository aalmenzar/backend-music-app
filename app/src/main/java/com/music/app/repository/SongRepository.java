package com.music.app.repository;

import com.music.app.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtist(String artist);
    List<Song> findByTitleContainingIgnoreCase(String title);
}
