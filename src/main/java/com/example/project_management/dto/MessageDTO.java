package com.example.project_management.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private String contenu;
    private String author;
    private LocalDateTime dateEnvoi;
    
    public MessageDTO() {}
    
    public MessageDTO(String contenu) {
        this.contenu = contenu;
    }
    
    public MessageDTO(String contenu, String author, LocalDateTime dateEnvoi) {
        this.contenu = contenu;
        this.author = author;
        this.dateEnvoi = dateEnvoi;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }
    
    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
