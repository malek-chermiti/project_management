package com.example.project_management.dto;

public class TaskChangeStateDTO {
    private String etat;
    
    public TaskChangeStateDTO() {}
    
    public TaskChangeStateDTO(String etat) {
        this.etat = etat;
    }
    
    public String getEtat() {
        return etat;
    }
    
    public void setEtat(String etat) {
        this.etat = etat;
    }
}
