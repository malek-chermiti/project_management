package com.example.project_management.dto;

public class MessageCreateDTO {
    private String contenu;
    
    public MessageCreateDTO() {}
    
    public MessageCreateDTO(String contenu) {
        this.contenu = contenu;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
