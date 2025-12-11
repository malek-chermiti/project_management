package com.example.project_management.dto;

import java.time.LocalDateTime;

public class TaskCreateDTO {
    private String titre;
    private String description;
    private LocalDateTime deadline;
    private Long priorite;
    
    public TaskCreateDTO() {}
    
    public TaskCreateDTO(String titre, String description, LocalDateTime deadline, Long priorite) {
        this.titre = titre;
        this.description = description;
        this.deadline = deadline;
        this.priorite = priorite;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    public Long getPriorite() {
        return priorite;
    }
    
    public void setPriorite(Long priorite) {
        this.priorite = priorite;
    }
}
