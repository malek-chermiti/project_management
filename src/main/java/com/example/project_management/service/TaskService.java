package com.example.project_management.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.project_management.repository.TaskRepository;
import com.example.project_management.repository.ProjetRepository;
import com.example.project_management.repository.UserRepository;
import com.example.project_management.model.Task;
import com.example.project_management.model.Projet;
import com.example.project_management.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjetRepository projetRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjetRepository projetRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projetRepository = projetRepository;
        this.userRepository = userRepository;
    }

    private static final Set<String> ETATS_AUTORISES = Set.of(
            "Todo",
            "en progres",
            "en verification",
            "terminee"
    );

    private void ensureEtatAllowed(String etat) {
        if (etat == null || !ETATS_AUTORISES.contains(etat)) {
            throw new IllegalArgumentException("Etat invalide. Valeurs autorisées: " + ETATS_AUTORISES);
        }
    }

    @Transactional(readOnly = true)
    public Task getById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @Transactional
    public Task creerTache(Long actorUserId,
                           Long projetId,
                           String titre,
                           String description,
                           LocalDateTime deadline,
                           Long priorite) {
        User actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Actor user not found"));
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new IllegalArgumentException("Projet not found"));

        boolean isMember = projet.getMembres().contains(actor);
        boolean isCreator = projet.getCreateur() != null && projet.getCreateur().getId().equals(actor.getId());
        if (!isMember && !isCreator) {
            throw new SecurityException("Not allowed to create tasks in this project");
        }
        Task task = new Task(titre, description, deadline, priorite, projet, List.of(), actor);
        // Link task to project and creator lists
        projet.getTaches().add(task);
        if (actor.getTasksCreees() != null) {
            actor.getTasksCreees().add(task);
        }
        Task saved = taskRepository.save(task);
        // Persist owning aggregates reflecting the relationship updates
        projetRepository.save(projet);
        userRepository.save(actor);
        return saved;
    }

    @Transactional
    public Task mettreAJourTache(Long actorUserId,
                                 Long taskId,
                                 String titre,
                                 String description,
                                 LocalDateTime deadline,
                                 Long priorite) {
        User actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Actor user not found"));
        Task task = getById(taskId);
        Projet projet = task.getProjet();

        boolean isMember = projet.getMembres().contains(actor);
        boolean isCreator = projet.getCreateur() != null && projet.getCreateur().getId().equals(actor.getId());
        boolean isAuthor = task.getAuteur() != null && task.getAuteur().getId().equals(actor.getId());
        if (!(isMember || isCreator || isAuthor)) {
            throw new SecurityException("Not allowed to update this task");
        }

        if (titre != null) task.setTitre(titre);
        if (description != null) task.setDescription(description);
        // Etat is managed exclusively via changerEtat()
        if (deadline != null) task.setDeadline(deadline);
        if (priorite != null) task.setPriorite(priorite);
        return taskRepository.save(task);
    }

    @Transactional
    public void changerEtat(Long actorUserId, Long taskId, String etat) {
        User actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Actor user not found"));
        Task task = getById(taskId);
        Projet projet = task.getProjet();

        boolean isMember = projet.getMembres().contains(actor);
        boolean isCreator = projet.getCreateur() != null && projet.getCreateur().getId().equals(actor.getId());
        boolean isAssignee = task.getAssignees() != null && task.getAssignees().contains(actor);
        if (!(isMember || isCreator || isAssignee)) {
            throw new SecurityException("Not allowed to change task state");
        }
        ensureEtatAllowed(etat);
        if ("terminee".equals(etat) && !isCreator) {
            throw new SecurityException("Only the project creator can set state to 'terminee'");
        }
        task.setEtat(etat);
        taskRepository.save(task);
    }

    @Transactional
    public void assigner(Long actorUserId, Long taskId, java.util.List<Long> userIds) {
        User actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Actor user not found"));
        Task task = getById(taskId);
        Projet projet = task.getProjet();

        boolean isCreator = projet.getCreateur() != null && projet.getCreateur().getId().equals(actor.getId());
        boolean isAuthor = task.getAuteur() != null && task.getAuteur().getId().equals(actor.getId());
        if (!(isCreator || isAuthor)) {
            throw new SecurityException("Only project creator or task author can assign");
        }

        for (Long uid : userIds) {
            User user = userRepository.findById(uid)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + uid));
            if (!task.getAssignees().contains(user)) {
                task.getAssignees().add(user);
            }
            if (user.getTasksAssignees() == null || !user.getTasksAssignees().contains(task)) {
                // ensure list exists and maintain inverse side
                if (user.getTasksAssignees() == null) {
                    user.setTasksAssignees(new java.util.ArrayList<>());
                }
                user.getTasksAssignees().add(task);
                userRepository.save(user);
            }
        }
        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> listerParProjet(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new IllegalArgumentException("Projet not found"));
        return projet.getTaches();
    }
}