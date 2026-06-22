package com.music.app.security;

public record AuthResponse(String token, String username, String role) {}
