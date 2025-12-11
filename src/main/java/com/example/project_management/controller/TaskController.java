package com.example.project_management.controller;

import com.example.project_management.model.Task;
import com.example.project_management.model.User;
import com.example.project_management.dto.TaskCreateDTO;
import com.example.project_management.dto.TaskUpdateDTO;
import com.example.project_management.dto.TaskResponseDTO;
import com.example.project_management.dto.TaskAssignDTO;
import com.example.project_management.dto.TaskChangeStateDTO;
import com.example.project_management.dto.MemberDTO;
import com.example.project_management.service.TaskService;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taches")
public class TaskController {
    
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
    
    // GET - Get task details by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        try {
            TaskResponseDTO task = taskService.getTaskDTO(id);
            return ResponseEntity.ok(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST - Create a new task
    @PostMapping("/{projetId}")
    public ResponseEntity<String> createTask(
            Authentication authentication,
            @PathVariable Long projetId,
            @RequestBody TaskCreateDTO taskData) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Task task = taskService.creerTache(
                    user.getId(),
                    projetId,
                    taskData.getTitre(),
                    taskData.getDescription(),
                    taskData.getDeadline(),
                    taskData.getPriorite()
            );
            return ResponseEntity.ok("Task created successfully with ID: " + task.getId());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PATCH - Update task
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody TaskUpdateDTO updates) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Task updated = taskService.mettreAJourTache(
                    user.getId(),
                    id,
                    updates.getTitre(),
                    updates.getDescription(),
                    updates.getDeadline(),
                    updates.getPriorite()
            );
            TaskResponseDTO response = taskService.getTaskDTO(updated.getId());
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE - Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            Authentication authentication,
            @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            taskService.deleteTache(user.getId(), id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // POST - Assign members to task
    @PostMapping("/{id}/assigner")
    public ResponseEntity<String> assignMembers(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody TaskAssignDTO assignData) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            taskService.assigner(user.getId(), id, assignData.getUserIds());
            return ResponseEntity.ok("Members assigned successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Get assigned members for task
    @GetMapping("/{id}/assignes")
    public ResponseEntity<List<MemberDTO>> getAssignedMembers(@PathVariable Long id) {
        try {
            List<MemberDTO> members = taskService.getMembresAssignes(id);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PATCH - Change task state
    @PatchMapping("/{id}/etat")
    public ResponseEntity<TaskResponseDTO> changeTaskState(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody TaskChangeStateDTO stateData) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            taskService.changerEtat(user.getId(), id, stateData.getEtat());
            TaskResponseDTO response = taskService.getTaskDTO(id);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
