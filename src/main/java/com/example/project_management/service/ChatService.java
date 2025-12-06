package com.example.project_management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_management.repository.ChatRepository;
import com.example.project_management.repository.ProjetRepository;
import com.example.project_management.model.Chat;
import com.example.project_management.model.Message;
import com.example.project_management.model.Projet;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final ProjetRepository projetRepository;
    

    public ChatService(ChatRepository chatRepository,
                       ProjetRepository projetRepository) {
        this.chatRepository = chatRepository;
        this.projetRepository = projetRepository;
    }

    @Transactional
    public Chat createChatForProject(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new IllegalArgumentException("Projet not found"));
        if (projet.getChat() != null) {
            return projet.getChat();
        }
        Chat chat = new Chat(projet);
        Chat saved = chatRepository.save(chat);
        projet.setChat(saved);
        projetRepository.save(projet);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Message> listMessages(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new IllegalArgumentException("Projet not found"));
        Chat chat = projet.getChat();
        if (chat == null) {
            return List.of();
        }
        // Basic listing; for large histories, add pagination on repository
        List<Message> all = chat.getMessages();
        int size = all.size();
        if (50 <= 0 || 50 >= size) return all;
        return all.subList(size - 50, size);
    }
}