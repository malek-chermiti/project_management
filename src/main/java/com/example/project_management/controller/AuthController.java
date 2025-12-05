package com.example.project_management.controller;

import com.example.project_management.security.JwtUtil;
import com.example.project_management.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", body.get("email"));
        String password = body.get("mot_de_passe");
        var user = userService.authenticate(email, password);
        if (user == null) throw new RuntimeException("Invalid credentials");
        return jwtUtil.generateToken(email);
    }
}
