package com.example.project_management.controller;

import com.example.project_management.model.Projet;
import com.example.project_management.model.User;
import com.example.project_management.model.Task;
import com.example.project_management.dto.ProjetUpdateDTO;
import com.example.project_management.dto.ProjetResponseDTO;
import com.example.project_management.dto.MemberDTO;
import com.example.project_management.dto.TaskResponseDTO;
import com.example.project_management.service.ProjetService;
import com.example.project_management.service.TaskService;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projets")
public class ProjetController {
    
    @Autowired
    private ProjetService projetService;
    
    @Autowired
    private TaskService taskService;
    
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
    
    // GET - Get project details by ID (user to open project)
    @GetMapping("/{id}")
    public ResponseEntity<ProjetResponseDTO> afficherDetails(@PathVariable Long id) {
        try {
            Projet projet = projetService.getById(id);
            ProjetResponseDTO response = new ProjetResponseDTO(
                projet.getId(),
                projet.getNom(),
                projet.getDescription(),
                projet.getCreateur().getNom()+" "+projet.getCreateur().getPrenom(),
                projet.getDateCreation()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // PATCH - Update project (nom, description)
    @PatchMapping("/{id}")
    public ResponseEntity<ProjetUpdateDTO> updateProjet(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody ProjetUpdateDTO updates) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Projet projet = projetService.getById(id);
            
            // Verify user is the creator
            if (!projet.getCreateur().getId().equals(user.getId())) {
                return ResponseEntity.status(403).build();
            }
            
            Projet updated = projetService.updateProjet(id, updates.getNom(), updates.getDescription());
            ProjetUpdateDTO response = new ProjetUpdateDTO(updated.getNom(), updated.getDescription());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Get all members of a project
    @GetMapping("/{id}/membres")
    public ResponseEntity<List<MemberDTO>> getMembers(@PathVariable Long id) {
        try {
            List<MemberDTO> members = projetService.getMembersDTO(id);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE - Remove a member from project
    @DeleteMapping("/{id}/membres/{userId}")
    public ResponseEntity<String> deleteMembre(
            Authentication authentication,
            @PathVariable Long id,
            @PathVariable Long userId) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            projetService.retirerMembre(id, userId, user.getId());
            return ResponseEntity.ok("Member removed successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST - Create a new project
    @PostMapping
    public ResponseEntity<String> createProjet(
            Authentication authentication,
            @RequestBody ProjetUpdateDTO projetData) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Projet projet = projetService.creerProjet(user.getId(), projetData.getNom(), projetData.getDescription());
            return ResponseEntity.ok("Project created successfully with ID: " + projet.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST - Join a project
    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinProjet(
            Authentication authentication,
            @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            projetService.ajouterMembre(id, user.getId());
            return ResponseEntity.ok("Successfully joined project");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST - Leave a project (ordinary member)
    @PostMapping("/{id}/leave")
    public ResponseEntity<String> leaveProjet(
            Authentication authentication,
            @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            projetService.leaveProjet(id, user.getId());
            return ResponseEntity.ok("Successfully left project");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE - Delete project (owner only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjet(
            Authentication authentication,
            @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            projetService.deleteProjet(id, user.getId());
            return ResponseEntity.ok("Project deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Get all tasks for a project
    @GetMapping("/{projetId}/taches")
    public ResponseEntity<List<TaskResponseDTO>> getProjectTasks(@PathVariable Long projetId) {
        try {
            List<Task> tasks = taskService.listerParProjet(projetId);
            List<TaskResponseDTO> response = tasks.stream()
                    .map(task -> taskService.getTaskDTO(task.getId()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
