package com.example.project_management.dto;

import com.example.project_management.model.Projet;
import com.example.project_management.model.Task;
import java.util.List;

public class UserResponseDTO {
    private String nom;
    private String prenom;
    private String email;
    private List<Projet> projetsCrees;
    private List<Projet> projets;
    private List<Task> tasksAssignees;
    
    public UserResponseDTO() {}
    
    public UserResponseDTO(String nom, String prenom, String email, 
                          List<Projet> projetsCrees, List<Projet> projets, 
                          List<Task> tasksAssignees) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.projetsCrees = projetsCrees;
        this.projets = projets;
        this.tasksAssignees = tasksAssignees;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<Projet> getProjetsCrees() {
        return projetsCrees;
    }
    
    public void setProjetsCrees(List<Projet> projetsCrees) {
        this.projetsCrees = projetsCrees;
    }
    
    public List<Projet> getProjets() {
        return projets;
    }
    
    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }
    
    public List<Task> getTasksAssignees() {
        return tasksAssignees;
    }
    
    public void setTasksAssignees(List<Task> tasksAssignees) {
        this.tasksAssignees = tasksAssignees;
    }
}
