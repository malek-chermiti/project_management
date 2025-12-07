package com.example.project_management.controller;

import com.example.project_management.model.User;
import com.example.project_management.model.Projet;
import com.example.project_management.dto.PasswordChangeDTO;
import com.example.project_management.service.UserService;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    // Helper - get current user from JWT token
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
    
    // POST - Sign in / Create user
    @PostMapping("/signin")
    public ResponseEntity<User> signin(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Projects joined by authenticated user
    @GetMapping("/projets-joined")
    public ResponseEntity<List<Projet>> getProjectsJoined(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            List<Projet> projets = userService.getMemberProjects(user.getId());
            return ResponseEntity.ok(projets);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET - Projects created by authenticated user
    @GetMapping("/projets-created")
    public ResponseEntity<List<Projet>> getProjectsCreated(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            List<Projet> projets = userService.getCreatedProjects(user.getId());
            return ResponseEntity.ok(projets);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PATCH - Update profile (nom,prenom) of authenticated user
    @PatchMapping("/profile")
    public ResponseEntity<User> updateProfile(
            Authentication authentication,
            @RequestBody User updates) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            User updated = userService.updateProfile(user.getId(), updates.getNom(),updates.getPrenom());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PATCH - Update password of authenticated user
    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(
            Authentication authentication,
            @RequestBody PasswordChangeDTO passwordChange) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            userService.changePassword(user.getId(), passwordChange.getOldPassword(), passwordChange.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(401).body("Old password is incorrect");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Get authenticated user profile
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            User profile = userService.getById(user.getId());
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
