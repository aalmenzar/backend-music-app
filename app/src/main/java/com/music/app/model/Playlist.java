package com.music.app.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistSong> items = new ArrayList<>();

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public User getUser() {
        return this.user;
    }

    public List<PlaylistSong> getItems() {
        return this.items;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItems(List<PlaylistSong> items) {
        this.items = items;
    }
}
