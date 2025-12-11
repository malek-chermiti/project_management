package com.example.project_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String etat;//Todo, en progres , en verification, terminee
    
    @Column(nullable = false)
    private LocalDateTime deadline;                         
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @Column(nullable = false)
    private Long priorite;
    
    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;
    
    @ManyToMany
    @JoinTable(
        name = "task_assignees",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "auteur_id", nullable = false)
    private User auteur;
    
    public Task() {
    }
    // Constructeur paramétré (sans dateCreation - générée automatiquement)
    public Task(String titre, String description, LocalDateTime deadline, 
                Long priorite, Projet projet, List<User> assignees, User auteur) {
        this.titre = titre;
        this.description = description;
        this.deadline = deadline;
        this.priorite = priorite;
        this.projet = projet;
        this.assignees = assignees;
        this.auteur = auteur;
        this.etat = "Todo"; // Etat initial par défaut
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getEtat() {
        return etat;
    }
    
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public Long getPriorite() {
        return priorite;
    }
    
    public Projet getProjet() {
        return projet;
    }
    
    public List<User> getAssignees() {
        return assignees;
    }
    
    public User getAuteur() {
        return auteur;
    }
    
    // Setters (pas de setter pour id)
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setEtat(String etat) {
        this.etat = etat;
    }
    
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    public void setPriorite(Long priorite) {
        this.priorite = priorite;
    }
    
    public void setProjet(Projet projet) {
        this.projet = projet;
    }
    
    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
    }
    
    public void setAuteur(User auteur) {
        this.auteur = auteur;
    }
    
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
