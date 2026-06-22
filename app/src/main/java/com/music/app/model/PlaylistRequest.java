package com.music.app.model;

import lombok.Setter;

@Setter
public class PlaylistRequest {

    private String name;

    public String getName() {
        return this.name;
    }
}
