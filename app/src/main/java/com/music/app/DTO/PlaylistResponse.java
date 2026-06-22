package com.music.app.DTO;

public class PlaylistResponse {

    private Long id;
    private String name;

    public PlaylistResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
