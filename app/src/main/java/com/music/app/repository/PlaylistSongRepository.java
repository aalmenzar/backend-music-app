package com.music.app.repository;

import com.music.app.model.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    List<PlaylistSong> findByPlaylistIdOrderByPositionAsc(Long playlistId);

    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);
}
