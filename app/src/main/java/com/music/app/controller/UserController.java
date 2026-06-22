package com.music.app.controller;

import com.music.app.model.User;
import com.music.app.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

