package com.example.project_management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_management.repository.ProjetRepository;
import com.example.project_management.repository.UserRepository;
import com.example.project_management.model.Projet;
import com.example.project_management.model.User;
import com.example.project_management.model.Task;
import com.example.project_management.dto.MemberDTO;
import java.util.stream.Collectors;

@Service
public class ProjetService {

	private final ProjetRepository projetRepository;
	private final UserRepository userRepository;
	private final ChatService chatService;

	public ProjetService(ProjetRepository projetRepository, UserRepository userRepository, ChatService chatService) {
		this.projetRepository = projetRepository;
		this.userRepository = userRepository;
		this.chatService = chatService;
	}

	public Projet getById(Long projetId) {
		return projetRepository.findById(projetId)
				.orElseThrow(() -> new IllegalArgumentException("Projet not found"));
	}

	@Transactional
	public Projet creerProjet(Long actorUserId, String nom, String description) {
		User createur = userRepository.findById(actorUserId)
				.orElseThrow(() -> new IllegalArgumentException("Creator user not found"));
		Projet projet = new Projet(nom, description);
		projet.setCreateur(createur);
        //add to creator's created projects
        createur.getProjetsCrees().add(projet);
		Projet saved = projetRepository.save(projet);
		userRepository.save(createur);
		chatService.createChatForProject(projet.getId());
		return saved;
	}

	@Transactional
	public void mettreAJourProjet(Long projetId, Long actorUserId, String nom, String description) {
		Projet projet = getById(projetId);
		User actor = userRepository.findById(actorUserId)
				.orElseThrow(() -> new IllegalArgumentException("Actor user not found"));

		if (projet.getCreateur() == null || !projet.getCreateur().getId().equals(actor.getId())) {
			throw new SecurityException("Only the project creator can update the project");
		}

		if (nom != null) projet.setNom(nom);
		if (description != null) projet.setDescription(description);
		projetRepository.save(projet);
	}

	@Transactional
	public void ajouterMembre(Long projetId, Long userId) {
		Projet projet = getById(projetId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		// Don't allow project owner to join
		if (projet.getCreateur().getId().equals(user.getId())) {
			throw new SecurityException("Project owner cannot join their own project");
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

	@Transactional(readOnly = true)
	public java.util.List<User> listerMembres(Long projetId) {
		Projet projet = getById(projetId);
		return projet.getMembres();
	}

	@Transactional(readOnly = true)
	public java.util.List<MemberDTO> getMembersDTO(Long projetId) {
		Projet projet = getById(projetId);
		return projet.getMembres().stream()
				.map(user -> new MemberDTO(user.getId(), user.getNom(), user.getPrenom(), user.getEmail()))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public java.util.List<Task> listerTaches(Long projetId) {
		Projet projet = getById(projetId);
		return projet.getTaches();
	}

	@Transactional
	public Projet updateProjet(Long projetId, String nom, String description) {
		Projet projet = getById(projetId);
		if (nom != null) {
			projet.setNom(nom);
		}
		if (description != null) {
			projet.setDescription(description);
		}
		return projetRepository.save(projet);
	}

	@Transactional
	public void leaveProjet(Long projetId, Long userId) {
		Projet projet = getById(projetId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// Project owner cannot leave
		if (projet.getCreateur().getId().equals(user.getId())) {
			throw new SecurityException("Project owner cannot leave the project. Delete the project instead.");
		}

		// Only remove if the user is currently a member
		boolean wasMember = projet.getMembres().remove(user);
		if (wasMember) {
			user.getProjets().remove(projet);
			projetRepository.save(projet);
			userRepository.save(user);
		}
	}

	@Transactional
	public void deleteProjet(Long projetId, Long actorUserId) {
		Projet projet = getById(projetId);
		User actor = userRepository.findById(actorUserId)
				.orElseThrow(() -> new IllegalArgumentException("Actor user not found"));

		// Only the creator can delete the project
		if (projet.getCreateur() == null || !projet.getCreateur().getId().equals(actor.getId())) {
			throw new SecurityException("Only the project creator can delete the project");
		}

		// Remove all members from project
		for (User member : new java.util.ArrayList<>(projet.getMembres())) {
			member.getProjets().remove(projet);
			userRepository.save(member);
		}
		projet.getMembres().clear();

		// Remove project from creator
		actor.getProjetsCrees().remove(projet);
		userRepository.save(actor);

		// Delete the project
		projetRepository.deleteById(projetId);
	}
}
