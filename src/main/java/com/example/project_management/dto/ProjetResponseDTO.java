package com.example.project_management.dto;

import java.time.LocalDateTime;

public class ProjetResponseDTO {
    private Long id;
    private String nom;
    private String description;
    private String createurNom;
    private LocalDateTime dateCreation;
    private Long chatId;
    
    public ProjetResponseDTO() {}
    
    public ProjetResponseDTO(Long id, String nom, String description, String createurNom, LocalDateTime dateCreation, Long chatId) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.createurNom = createurNom;
        this.dateCreation = dateCreation;
        this.chatId = chatId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCreateurNom() {
        return createurNom;
    }
    
    public void setCreateurNom(String createurNom) {
        this.createurNom = createurNom;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Long getChatId() {
        return chatId;
    }
    
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
