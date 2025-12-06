package com.example.project_management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_management.repository.MessageRepository;
import com.example.project_management.repository.ChatRepository;
import com.example.project_management.repository.UserRepository;
import com.example.project_management.model.Message;
import com.example.project_management.model.Chat;
import com.example.project_management.model.User;
import com.example.project_management.model.Projet;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          ChatRepository chatRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Message envoyerMessage(Long actorUserId, Long chatId, String contenu) {
        User actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Actor user not found"));
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        // Authorization: only members or creator of the related project can send messages
        Projet projet = chat.getProjet();
        boolean isMember = projet != null && projet.getMembres().contains(actor);
        boolean isCreator = projet != null && projet.getCreateur() != null && projet.getCreateur().getId().equals(actor.getId());
        if (!isMember && !isCreator) {
            throw new SecurityException("Not allowed to send messages in this chat");
        }

        Message message = new Message(contenu, actor, chat);
        Message saved = messageRepository.save(message);
        chat.getMessages().add(saved);
        actor.getMessages().add(saved);
        chatRepository.save(chat);
        userRepository.save(actor);
        return saved;
    }
}