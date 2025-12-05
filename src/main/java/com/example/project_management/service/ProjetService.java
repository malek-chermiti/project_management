package com.example.project_management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_management.repository.ProjetRepository;
import com.example.project_management.repository.UserRepository;
import com.example.project_management.model.Projet;
import com.example.project_management.model.User;

@Service
public class ProjetService {

	private final ProjetRepository projetRepository;
	private final UserRepository userRepository;

	public ProjetService(ProjetRepository projetRepository, UserRepository userRepository) {
		this.projetRepository = projetRepository;
		this.userRepository = userRepository;
	}

	public Projet getById(Long projetId) {
		return projetRepository.findById(projetId)
				.orElseThrow(() -> new IllegalArgumentException("Projet not found"));
	}

	@Transactional
	public Projet creerProjet(String email, String nom, String description) {
		User createur = userRepository.findByEmail(email);
        if (createur == null) {
            throw new IllegalArgumentException("Creator user not found");
        }
		Projet projet = new Projet(nom, description);
		projet.setCreateur(createur);
        //add to creator's created projects
        createur.getProjetsCrees().add(projet);
		Projet saved = projetRepository.save(projet);
		userRepository.save(createur);
		return saved;
	}

	@Transactional
	public void mettreAJourProjet(Long projetId, String nom, String description) {
		Projet projet = getById(projetId);
		if (nom != null) projet.setNom(nom);
		if (description != null) projet.setDescription(description);
		projetRepository.save(projet);
	}

	@Transactional
	public void ajouterMembre(Long projetId, String email) {
		Projet projet = getById(projetId);
		User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
		if (!projet.getMembres().contains(user)) {
			projet.getMembres().add(user);
		}
		if (!user.getProjets().contains(projet)) {
			user.getProjets().add(projet);
		}
		projetRepository.save(projet);
		userRepository.save(user);
	}

	@Transactional
	public void retirerMembre(Long projetId, Long targetUserId, Long actorUserId) {
		Projet projet = getById(projetId);
		User actor = userRepository.findById(actorUserId)
				.orElseThrow(() -> new IllegalArgumentException("Actor user not found"));
		User target = userRepository.findById(targetUserId)
				.orElseThrow(() -> new IllegalArgumentException("Target user not found"));

		// Only the creator can remove members
		if (projet.getCreateur() == null || !projet.getCreateur().getId().equals(actor.getId())) {
			throw new SecurityException("Only the project creator can remove members");
		}

		// Only remove if the target is currently a member
		boolean wasMember = projet.getMembres().remove(target);
		if (wasMember) {
			target.getProjets().remove(projet);
			projetRepository.save(projet);
			userRepository.save(target);
		}
	}

	@Transactional
	public Projet accederProjet(Long userId, Long projetId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		Projet projet = getById(projetId);
		boolean isMember = projet.getMembres().contains(user);
		boolean isCreator = projet.getCreateur() != null && projet.getCreateur().getId().equals(user.getId());
		if (!isMember && !isCreator) {
			throw new SecurityException("Access denied to this project");
		}
		return projet;
	}
}
