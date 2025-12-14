# Project Management API

## Description
Backend REST pour la gestion de projets, tâches et messagerie interne. Les utilisateurs peuvent créer des projets, inviter des membres, créer/assigner des tâches, et échanger des messages dans le chat du projet. Authentification via JWT, sécurité stateless, et CORS configuré pour le développement.

## Tech Stack
- Spring Boot (Java)
- Spring Data JPA (Hibernate)
- Spring Security (JWT)
- MySQL
- Maven

## Base URL
- Development: `http://localhost:9090` 

## Authentication
- JWT stateless
- Endpoint de login: `POST /auth/login` avec email et mot de passe → retourne un token JWT
- Les endpoints protégés exigent `Authorization: Bearer <token>`

## Key Endpoints
- Projects
  - `POST /api/projects` — créer un projet (créateur)
    - Request (JSON):
      ```json
      { "nom": "Projet A", "description": "Desc" }
      ```
    - Response (JSON):
      ```json
      { "id": 1, "nom": "Projet A", "description": "Desc", "createurNomComplet": "Nom Prenom", "dateCreation": "2025-12-14T10:00:00", "chatId": 5 }
      ```
  - `PUT /api/projects/{id}` — mettre à jour (créateur uniquement)
    - Request (JSON):
      ```json
      { "nom": "Projet A v2", "description": "Nouvelle desc" }
      ```
    - Response (JSON):
      ```json
      { "nom": "Projet A v2", "description": "Nouvelle desc" }
      ```
  - `POST /api/projects/{id}/members` — rejoindre un projet
    - Request: Authorization Bearer token
    - Response (JSON):
      ```json
      { "message": "Successfully joined project" }
      ```
  - `DELETE /api/projects/{id}/members/{userId}` — retirer un membre (créateur seulement)
    - Request: Authorization Bearer token
    - Response (JSON):
      ```json
      { "message": "Member removed successfully" }
      ```
  - `POST /api/projects/{id}/leave` — quitter le projet (membre; désassignation de ses tâches)
    - Request: Authorization Bearer token
    - Response (JSON):
      ```json
      { "message": "Successfully left project" }
      ```
  - `DELETE /api/projects/{id}` — supprimer le projet (cascade supprime tâches + messages)
    - Request: Authorization Bearer token
    - Response (JSON):
      ```json
      { "message": "Project deleted successfully" }
      ```
- Tasks
  - `POST /api/projects/{id}/tasks` — créer une tâche
    - Request (JSON):
      ```json
      { "titre": "Tâche 1", "description": "Faire X", "deadline": "2025-12-31T23:59:00", "priorite": 2 }
      ```
    - Response (JSON):
      ```json
      { "message": "Task created successfully", "id": 101 }
      ```
  - `PUT /api/tasks/{taskId}` — mettre à jour (sans changer l’état)
    - Request (JSON):
      ```json
      { "titre": "Tâche 1 bis", "description": "Faire Y", "deadline": "2026-01-15T12:00:00", "priorite": 3 }
      ```
    - Response (JSON):
      ```json
      { "id": 101, "titre": "Tâche 1 bis", "description": "Faire Y", "etat": "Todo", "deadline": "2026-01-15T12:00:00", "priorite": 3, "projetId": 1, "auteurId": 12, "assigneeIds": [13, 14] }
      ```
  - `PATCH /api/tasks/{taskId}/state` — changer l’état (créateur seul pour `terminee`)
    - Request (JSON):
      ```json
      { "etat": "en progres" }
      ```
    - Response (JSON):
      ```json
      { "id": 101, "titre": "Tâche 1", "description": "Faire X", "etat": "en progres", "deadline": "2025-12-31T23:59:00", "priorite": 2, "projetId": 1, "auteurId": 12, "assigneeIds": [13, 14] }
      ```
  - `POST /api/tasks/{taskId}/assignees` — assigner plusieurs membres
    - Request (JSON):
      ```json
      { "userIds": [13, 15] }
      ```
    - Response (JSON):
      ```json
      { "message": "Members assigned successfully" }
      ```
  - `GET /api/projects/{id}/tasks` — lister les tâches du projet
    - Response (JSON):
      ```json
      [
        { "id": 101, "titre": "Tâche 1", "description": "Faire X", "etat": "Todo", "deadline": "2025-12-31T23:59:00", "priorite": 2, "projetId": 1, "auteurId": 12, "assigneeIds": [13, 14] },
        { "id": 102, "titre": "Tâche 2", "description": "Faire Z", "etat": "en progres", "deadline": "2026-01-05T12:00:00", "priorite": 1, "projetId": 1, "auteurId": 12, "assigneeIds": [] }
      ]
      ```
- Messages
  - `POST /messages/{chatId}` — envoyer un message
    - Request (JSON):
      ```json
      { "contenu": "Hello team" }
      ```
    - Response (JSON):
      ```json
      { "contenu": "Hello team", "auteur": "Nom Prenom", "dateEnvoi": "2025-12-14T10:05:00" }
      ```
  - `GET /messages/{chatId}` — lister les messages du chat
    - Response (JSON):
      ```json
      [
        { "contenu": "Hello team", "auteur": "Nom Prenom", "dateEnvoi": "2025-12-14T10:05:00" },
        { "contenu": "Update done", "auteur": "Autre Membre", "dateEnvoi": "2025-12-14T10:06:00" }
      ]
      ```

## Environment Variables
Configurer via variables système ou `.env` (non commité). Principales variables:
- `DB_URL` — JDBC URL MySQL (ex: `jdbc:mysql://localhost:3306/project_management_db`)
- `DB_USERNAME` — utilisateur MySQL
- `DB_PASSWORD` — mot de passe MySQL

`src/main/resources/application.properties` lit ces variables via `${...}`.

## How to Run
1. Préparer la base MySQL et un utilisateur.
2. Définir les variables d’environnement (Windows PowerShell):

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/project_management_db"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
```

3. Construire et lancer:

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

4. Tester l’authentification:
- `POST /auth/login` → récupérez le JWT
- Appelez les endpoints protégés avec `Authorization: Bearer <token>`

## Notes
- En production, restreindre CORS à des origines précises et gérer les variables via un gestionnaire sécurisé.
- La suppression d’un projet supprime aussi son chat et tous les messages; les tâches du projet sont supprimées via cascade.