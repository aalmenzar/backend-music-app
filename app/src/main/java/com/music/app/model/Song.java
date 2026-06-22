package com.music.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String artist;

    private Integer durationSeconds;

    private String filePath;

    private String mimeType;

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public Integer getDurationSeconds() {
        return this.durationSeconds;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getMimeType() {
        return this.mimeType;
    }
}
