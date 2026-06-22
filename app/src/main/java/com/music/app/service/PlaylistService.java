package com.music.app.service;

import com.music.app.DTO.PlaylistResponse;
import com.music.app.DTO.SongResponse;
import com.music.app.model.*;
import com.music.app.repository.PlaylistRepository;
import com.music.app.repository.PlaylistSongRepository;
import com.music.app.repository.SongRepository;
import com.music.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistSongRepository playlistSongRepository;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, SongRepository songRepository, PlaylistSongRepository playlistSongRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.playlistSongRepository = playlistSongRepository;
    }

    public List<PlaylistResponse> getPlaylistsByUser(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return playlistRepository.findByUser(user)
                .stream()
                .map(p -> new PlaylistResponse(p.getId(), p.getName()))
                .toList();
    }

    public Playlist createForUser(PlaylistRequest request, String username) {

        User user = findUser(username);
        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setUser(user);

        return playlistRepository.save(playlist);
    }

    public void deleteIfOwner(Long playlistId, String username) {

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist no encontrada"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar esta playlist");
        }

        playlistRepository.delete(playlist);
    }

    public List<SongResponse> getSongsFromPlaylist(Long playlistId) {

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist no encontrada"));

        return playlist.getItems()
                .stream()
                .map(ps -> new SongResponse(
                        ps.getSong().getId(),
                        ps.getSong().getTitle(),
                        ps.getSong().getArtist(),
                        ps.getSong().getDurationSeconds(),
                        ps.getSong().getFilePath(),
                        ps.getSong().getMimeType()
                ))
                .toList();
    }

    public void addSongToPlaylist(Long playlistId, Long songId, String username) {

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist no encontrada"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes modificar esta playlist");
        }

        if (playlistSongRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {return;}

        Song song = songRepository.findById(songId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canción no encontrada"));

        int nextPosition = playlistSongRepository.findByPlaylistIdOrderByPositionAsc(playlistId).size() + 1;
        PlaylistSong ps = new PlaylistSong();
        ps.setPlaylist(playlist);
        ps.setSong(song);
        ps.setPosition(nextPosition);

        playlistSongRepository.save(ps);
    }

    public void removeSongFromPlaylist(Long playlistId, Long songId, String username) {

        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Playlist no encontrada"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes modificar esta playlist");
        }

        PlaylistSong ps = playlistSongRepository
                .findAll()
                .stream()
                .filter(x ->
                        x.getPlaylist().getId().equals(playlistId)
                                && x.getSong().getId().equals(songId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canción no encontrada en la playlist"));

        playlistSongRepository.delete(ps);
        List<PlaylistSong> remaining = playlistSongRepository.findByPlaylistIdOrderByPositionAsc(playlistId);

        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setPosition(i + 1);
        }

        playlistSongRepository.saveAll(remaining);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

}
