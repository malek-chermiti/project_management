package com.example.project_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "projets")
public class Projet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "createur_id", nullable = false)
    private User createur;
    
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taches = new ArrayList<>();
    
    @OneToOne(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;
    
    @ManyToMany(mappedBy = "projets")
    private List<User> membres = new ArrayList<>(); 

    public Projet() {
    }
    
     // Constructeur paramétré (sans dateCreation - générée automatiquement)
    public Projet(String nom, String description) {
        this.nom = nom;
        this.description = description;
       
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    
    public User getCreateur() {
        return createur;
    }
    
    public List<Task> getTaches() {
        return taches;
    }
    
    public Chat getChat() {
        return chat;
    }
    
    public List<User> getMembres() {
        return membres;
    }
    
    // Setters (pas de setter pour id et dateCreation)
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setCreateur(User createur) {
        this.createur = createur;
    }
    
    public void setTaches(List<Task> taches) {
        this.taches = taches;
    }
    
    public void setChat(Chat chat) {
        this.chat = chat;
    }
    
    public void setMembres(List<User> membres) {
        this.membres = membres;
    }
    
  
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
