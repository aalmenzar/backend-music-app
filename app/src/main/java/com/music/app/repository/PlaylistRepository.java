package com.music.app.repository;

import com.music.app.model.Playlist;
import com.music.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
}
