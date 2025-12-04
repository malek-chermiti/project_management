package com.example.project_management.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;
    private Date dateEnvoi = null;

    // --- Relations ---
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private User auteur;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    // --- Constructors ---
    public Message() {}

    public Message(String contenu, User auteur, Chat chat) {
        this.contenu = contenu;
        this.auteur = auteur;
        this.chat = chat;
    }

    public Long getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public User getAuteur() {
        return auteur;
    }

    public void setAuteur(User auteur) {
        this.auteur = auteur;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @PrePersist
    protected void onCreate() {
        if (this.dateEnvoi == null) {
            this.dateEnvoi = new Date();
        }
    }
}
