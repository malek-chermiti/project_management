package com.example.project_management.controller;

import com.example.project_management.model.User;
import com.example.project_management.model.Projet;
import com.example.project_management.dto.PasswordChangeDTO;
import com.example.project_management.dto.ProfileUpdateDTO;
import com.example.project_management.dto.ProjetResponseDTO;
import com.example.project_management.service.UserService;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> signin(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Projects joined by authenticated user
    @GetMapping("/projets-joined")
    public ResponseEntity<List<ProjetResponseDTO>> getProjectsJoined(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            List<Projet> projets = userService.getMemberProjects(user.getId());
            List<ProjetResponseDTO> response = projets.stream()
                .map(p -> new ProjetResponseDTO(p.getId(), p.getNom(), p.getDescription(), 
                    p.getCreateur().getNom()+" "+p.getCreateur().getPrenom(), p.getDateCreation()))
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // GET - Projects created by authenticated user
    @GetMapping("/projets-created")
    public ResponseEntity<List<ProjetResponseDTO>> getProjectsCreated(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            List<Projet> projets = userService.getCreatedProjects(user.getId());
            List<ProjetResponseDTO> response = projets.stream()
                .map(p -> new ProjetResponseDTO(p.getId(), p.getNom(), p.getDescription(), 
                    p.getCreateur().getNom()+" "+p.getCreateur().getPrenom(), p.getDateCreation()))
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PATCH - Update profile (nom,prenom) of authenticated user
    @PatchMapping("/profile")
    public ResponseEntity<ProfileUpdateDTO> updateProfile(
            Authentication authentication,
            @RequestBody ProfileUpdateDTO updates) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            User updated = userService.updateProfile(user.getId(), updates.getNom(), updates.getPrenom());
            ProfileUpdateDTO response = new ProfileUpdateDTO(updated.getNom(), updated.getPrenom());
            return ResponseEntity.ok(response);
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
    public ResponseEntity<ProfileUpdateDTO> getProfile(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            User profile = userService.getById(user.getId());
            ProfileUpdateDTO response = new ProfileUpdateDTO(profile.getNom(), profile.getPrenom(), profile.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
