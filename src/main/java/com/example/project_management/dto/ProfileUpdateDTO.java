package com.example.project_management.dto;

public class ProfileUpdateDTO {
    private String nom;
    private String prenom;
    
    public ProfileUpdateDTO() {}
    
    public ProfileUpdateDTO(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
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
}
