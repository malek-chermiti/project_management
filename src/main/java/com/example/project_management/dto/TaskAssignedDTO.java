package com.example.project_management.dto;

import java.time.LocalDateTime;

public class TaskAssignedDTO {
    private Long id;
    private String titre;
    private String etat;
    private LocalDateTime deadline;
    private Long priorite;
    private String projectTitre;
    
    public TaskAssignedDTO() {}
    
    public TaskAssignedDTO(Long id, String titre, String etat, LocalDateTime deadline, Long priorite, String projectTitre) {
        this.id = id;
        this.titre = titre;
        this.etat = etat;
        this.deadline = deadline;
        this.priorite = priorite;
        this.projectTitre = projectTitre;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getEtat() {
        return etat;
    }
    
    public void setEtat(String etat) {
        this.etat = etat;
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
    
    public String getProjectTitre() {
        return projectTitre;
    }
    
    public void setProjectTitre(String projectTitre) {
        this.projectTitre = projectTitre;
    }
}
