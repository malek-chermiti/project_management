package com.example.project_management.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_management.repository.UserRepository;
import com.example.project_management.model.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) { 
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        String hashedPassword = encoder.encode(user.getMotDePasse());
        user.setMotDePasse(hashedPassword);
        return userRepository.save(user);
    }

    public User authenticate(String email, String motDePasse) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        return encoder.matches(motDePasse, user.getMotDePasse()) ? user : null;
    }

    // SRP: UserService manages user lifecycle and profile operations
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User updateProfile(Long userId, String nom, String prenom) {
        User user = getById(userId);
        if (nom != null) user.setNom(nom);
        if (prenom != null) user.setPrenom(prenom);

        return userRepository.save(user);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (!encoder.matches(oldPassword, user.getMotDePasse())) {
            throw new SecurityException("Invalid current password");
        }
        user.setMotDePasse(encoder.encode(newPassword));
        userRepository.save(user);
    }
    // les projets joined
    @Transactional(readOnly = true)
    public java.util.List<Projet> getMemberProjects(Long userId) {
        User user = getById(userId);
        return user.getProjets();
    }

    @Transactional(readOnly = true)
    public java.util.List<Projet> getCreatedProjects(Long userId) {
        User user = getById(userId);
        return user.getProjetsCrees();
    }
    
}
