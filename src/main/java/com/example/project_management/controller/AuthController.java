

package com.example.project_management.controller;

import com.example.project_management.dto.DTOLogin;
import com.example.project_management.security.JwtUtil;
import com.example.project_management.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> login(@RequestBody DTOLogin body) {
        try {
            String email = body.getEmail();
            String password = body.getMot_de_passe();
            
            var user = userService.authenticate(email, password);
            if (user == null) {
                throw new SecurityException("Invalid email or password");
            }
            
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(token);
        } catch (SecurityException e) {
            throw e;
        }
    }
}
