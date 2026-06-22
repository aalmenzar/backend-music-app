package com.music.app.DTO;

public class SongResponse {

    private Long id;
    private String title;
    private String artist;
    private Integer durationSeconds;
    private String filePath;
    private String mimeType;

    public SongResponse(Long id, String title, String artist, Integer durationSeconds, String filePath, String mimeType) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationSeconds = durationSeconds;
        this.filePath = filePath;
        this.mimeType = mimeType;
    }

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

    @Override
    public String toString() {
            return title + " - " + artist;
        }


}
