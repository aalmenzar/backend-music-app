package com.music.app.DTO;

public class PlaylistSongResponse {

    private Long id;
    private Long songId;
    private String title;
    private String artist;
    private int position;

    public PlaylistSongResponse(Long id, Long songId, String title, String artist, int position) {
        this.id = id;
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public Long getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getPosition() {
        return position;
    }
}
