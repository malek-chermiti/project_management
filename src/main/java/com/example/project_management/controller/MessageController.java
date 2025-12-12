package com.example.project_management.controller;

import com.example.project_management.model.Message;
import com.example.project_management.model.User;
import com.example.project_management.dto.MessageDTO;
import com.example.project_management.dto.MessageCreateDTO;
import com.example.project_management.service.MessageService;
import com.example.project_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
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
    
    // POST - Send a message to a chat
    @PostMapping("/{chatId}")
    public ResponseEntity<MessageDTO> sendMessage(
            Authentication authentication,
            @PathVariable Long chatId,
            @RequestBody MessageCreateDTO messageCreateDTO) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Message message = messageService.envoyerMessage(user.getId(), chatId, messageCreateDTO.getContenu());
            MessageDTO response = new MessageDTO(
                message.getContenu(),
                message.getAuteur().getNom() + " " + message.getAuteur().getPrenom(),
                message.getDateEnvoi()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET - Get all messages for a chat by chatId
    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChat(@PathVariable Long chatId) {
        try {
            List<Message> messages = messageService.getMessagesByChat(chatId);
            List<MessageDTO> response = messages.stream()
                .map(m -> new MessageDTO(
                    m.getContenu(),
                    m.getAuteur().getNom() + " " + m.getAuteur().getPrenom(),
                    m.getDateEnvoi()
                ))
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
