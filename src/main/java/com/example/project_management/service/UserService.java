package com.example.project_management.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

        userRepository.save(user);
        return user;
    }

    public User authenticate(String email, String motDePasse) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        return encoder.matches(motDePasse, user.getMotDePasse()) ? user : null;
    }

    public void forgotPassword(String email) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Projet creerProjet(Long userId, String nom, String description) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void rejoindreProjet(Long userId, Long projetId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Projet accederProjet(Long userId, Long projetId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Task gererTache(Long userId, Task task) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void changerEtatTache(Long userId, Long taskId, String etat) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Message envoyerMessage(Long userId, Long chatId, String contenu) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
