package com.example.project_management.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Column(unique = true, nullable = false)
    private String email;

    private String motDePasse;

    // --- Relations ---

    // Projects created by the user (optional)
    @OneToMany(mappedBy = "createur")
    private List<Projet> projetsCrees = new ArrayList<>();

    public List<Projet> getProjetsCrees() {
        return projetsCrees;
    }

    public void setProjetsCrees(List<Projet> projetsCrees) {
        this.projetsCrees = projetsCrees;
    }

    // Projects where the user is a member (Many-to-Many via join table)
    @ManyToMany
    @JoinTable(
        name = "user_project",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Projet> projets = new ArrayList<>();
    // Tasks assigned to the user
    @ManyToMany(mappedBy = "assignees")
    private List<Task> tasksAssignees = new ArrayList<>();

    // Tasks created by the user
    @OneToMany(mappedBy = "auteur")
    private List<Task> tasksCreees = new ArrayList<>();

    // Messages sent by the user
    @OneToMany(mappedBy = "auteur")
    private List<Message> messages = new ArrayList<>();

    // --- Constructors ---
    public User() {}

    public User(String nom,String prenom ,String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<Projet> getProjets() {
        return projets;
    }

    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }
}

