package com.music.app.DTO;

import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String username;

    public UserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}