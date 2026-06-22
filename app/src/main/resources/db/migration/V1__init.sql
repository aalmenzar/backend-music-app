CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL
);

CREATE TABLE playlists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_playlists_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE songs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255),
    duration_seconds INT,
    file_path VARCHAR(500),
    mime_type VARCHAR(100)
);

CREATE TABLE playlist_songs (
    id SERIAL PRIMARY KEY,
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    position INT,
    CONSTRAINT fk_playlistsongs_playlists
        FOREIGN KEY (playlist_id)
        REFERENCES playlists(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_playlistsongs_songs
        FOREIGN KEY (song_id)
        REFERENCES songs(id)
        ON DELETE CASCADE
);