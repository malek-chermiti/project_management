package com.example.project_management.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TaskResponseDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDateTime deadline;
    private Long priorite;
    private String etat;
    private String auteur;
    private LocalDateTime dateCreation;
    private List<MemberDTO> assignees;
    
    public TaskResponseDTO() {}
    
    public TaskResponseDTO(Long id, String titre, String description, LocalDateTime deadline, 
                          Long priorite, String etat, String auteur, LocalDateTime dateCreation, 
                          List<MemberDTO> assignees) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.deadline = deadline;
        this.priorite = priorite;
        this.etat = etat;
        this.auteur = auteur;
        this.dateCreation = dateCreation;
        this.assignees = assignees;
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
    
    public String getEtat() {
        return etat;
    }
    
    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    public String getAuteur() {
        return auteur;
    }
    
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public List<MemberDTO> getAssignees() {
        return assignees;
    }
    
    public void setAssignees(List<MemberDTO> assignees) {
        this.assignees = assignees;
    }
}
