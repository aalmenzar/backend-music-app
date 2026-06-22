package com.music.app.controller;

import com.music.app.DTO.UserResponse;
import com.music.app.model.RegisterRequest;
import com.music.app.model.User;
import com.music.app.security.AuthRequest;
import com.music.app.security.AuthResponse;
import com.music.app.service.JwtService;
import com.music.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }

        var user = userService.findByUsername(request.username()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token,user.getUsername(), user.getRole().name());
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        User saved = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(saved.getId(), saved.getUsername()));
    }
}


