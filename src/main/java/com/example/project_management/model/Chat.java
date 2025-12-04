package com.example.project_management.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "chats")
public class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relations ---
    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "projet_id", unique = true)
    private Projet projet;

    // --- Constructors ---
    public Chat() {}

    public Chat(Projet projet) {
        this.projet = projet;
    }

    public Long getId() {
        return id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }
}
