package com.music.app.controller;

import com.music.app.DTO.PlaylistResponse;
import com.music.app.DTO.SongResponse;
import com.music.app.model.CustomUserDetails;
import com.music.app.model.Playlist;
import com.music.app.model.PlaylistRequest;
import com.music.app.service.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/user/{username}")
    public List<PlaylistResponse> getPlaylistsByUser(@PathVariable String username) {
        return playlistService.getPlaylistsByUser(username);
    }

    @PostMapping
    public ResponseEntity<PlaylistResponse> create(@RequestBody PlaylistRequest request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Playlist created = playlistService.createForUser(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PlaylistResponse(created.getId(), created.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetails  userDetails) {
        playlistService.deleteIfOwner(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        playlistService.addSongToPlaylist(playlistId, songId, userDetails.getUsername());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        playlistService.removeSongFromPlaylist(playlistId, songId, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{playlistId}/songs")
    public List<SongResponse> getSongsFromPlaylist(
            @PathVariable Long playlistId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return playlistService.getSongsFromPlaylist(playlistId);
    }

}

