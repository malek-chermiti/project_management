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
  - Production: `https://projectmanagement-production-d023.up.railway.app`
  - Development: `http://localhost:9090` 

## Key Endpoints
- Auth
  - `POST /auth/login` — authentification
    - Request (JSON):
      ```json
      { "email": "user@example.com", "mot_de_passe": "secret" }
      ```
    - Response (text): JWT token string

- Projets
  - `GET /projets/{id}` — détails d’un projet
    - Response (JSON):
      ```json
      { "id": 1, "nom": "Projet A", "description": "Desc", "createurNom": "Nom Prenom", "dateCreation": "2025-12-14T10:00:00", "chatId": 5 }
      ```
  - `PATCH /projets/{id}` — mettre à jour (créateur uniquement)
    - Request (JSON):
      ```json
      { "nom": "Projet A v2", "description": "Nouvelle desc" }
      ```
    - Response (JSON):
      ```json
      { "nom": "Projet A v2", "description": "Nouvelle desc" }
      ```
  - `GET /projets/{id}/membres` — lister les membres
    - Response (JSON):
      ```json
      [ { "id": 12, "nom": "Doe", "prenom": "John", "email": "john@example.com" } ]
      ```
  - `DELETE /projets/{id}/membres/{userId}` — retirer un membre (créateur)
    - Response (text): Member removed successfully
  - `POST /projets` — créer un projet (créateur)
    - Request (JSON):
      ```json
      { "nom": "Projet A", "description": "Desc" }
      ```
    - Response (text): Project created successfully with ID: 1
  - `POST /projets/{id}/join` — rejoindre un projet
    - Response (text): Successfully joined project
  - `POST /projets/{id}/leave` — quitter le projet (membre; désassignation de ses tâches)
    - Response (text): Successfully left project
  - `DELETE /projets/{id}` — supprimer le projet (cascade supprime tâches + messages)
    - Response (text): Project deleted successfully
  - `GET /projets/{projetId}/taches` — lister les tâches du projet
    - Response (JSON):
      ```json
      [
        { "id": 101, "titre": "Tâche 1", "description": "Faire X", "deadline": "2025-12-31T23:59:00", "priorite": 2, "etat": "Todo", "auteur": "Nom Prenom", "dateCreation": "2025-12-01T10:00:00", "assignees": [ { "id": 12, "nom": "Doe", "prenom": "John", "email": "john@example.com" } ] },
        { "id": 102, "titre": "Tâche 2", "description": "Faire Z", "deadline": "2026-01-05T12:00:00", "priorite": 1, "etat": "en progres", "auteur": "Nom Prenom", "dateCreation": "2025-12-02T09:30:00", "assignees": [] }
      ]
      ```

- Tâches
  - `GET /taches/{id}` — détails d’une tâche
    - Response (JSON):
      ```json
      { "id": 101, "titre": "Tâche 1", "description": "Faire X", "deadline": "2025-12-31T23:59:00", "priorite": 2, "etat": "Todo", "auteur": "Nom Prenom", "dateCreation": "2025-12-01T10:00:00", "assignees": [ { "id": 12, "nom": "Doe", "prenom": "John", "email": "john@example.com" } ] }
      ```
  - `POST /taches/{projetId}` — créer une tâche
    - Request (JSON):
      ```json
      { "titre": "Tâche 1", "description": "Faire X", "deadline": "2025-12-31T23:59:00", "priorite": 2 }
      ```
    - Response (text): Task created successfully with ID: 101
  - `PATCH /taches/{id}` — mettre à jour (sans changer l’état)
    - Request (JSON):
      ```json
      { "titre": "Tâche 1 bis", "description": "Faire Y", "deadline": "2026-01-15T12:00:00", "priorite": 3 }
      ```
    - Response (JSON):
      ```json
      { "id": 101, "titre": "Tâche 1 bis", "description": "Faire Y", "deadline": "2026-01-15T12:00:00", "priorite": 3, "etat": "Todo", "auteur": "Nom Prenom", "dateCreation": "2025-12-01T10:00:00", "assignees": [ { "id": 12, "nom": "Doe", "prenom": "John", "email": "john@example.com" } ] }
      ```
  - `DELETE /taches/{id}` — supprimer une tâche
    - Response (text): Task deleted successfully
  - `POST /taches/{id}/assigner` — assigner plusieurs membres
    - Request (JSON):
      ```json
      { "userIds": [13, 15] }
      ```
    - Response (text): Members assigned successfully
  - `GET /taches/{id}/assignes` — membres assignés
    - Response (JSON):
      ```json
      [ { "id": 12, "nom": "Doe", "prenom": "John", "email": "john@example.com" } ]
      ```
  - `PATCH /taches/{id}/etat` — changer l’état (créateur seul pour "terminee")
    - Request (JSON):
      ```json
      { "etat": "en progres" }
      ```
    - Response (JSON):
      ```json
      { "id": 101, "titre": "Tâche 1", "description": "Faire X", "deadline": "2025-12-31T23:59:00", "priorite": 2, "etat": "en progres", "auteur": "Nom Prenom", "dateCreation": "2025-12-01T10:00:00", "assignees": [ { "id": 12, "nom": "Doe", "prenom": "John", "email": "john@example.com" } ] }
      ```

- Messages
  - `POST /messages/{chatId}` — envoyer un message
    - Request (JSON):
      ```json
      { "contenu": "Hello team" }
      ```
    - Response (JSON):
      ```json
      { "contenu": "Hello team", "author": "Nom Prenom", "dateEnvoi": "2025-12-14T10:05:00" }
      ```
  - `GET /messages/{chatId}` — lister les messages du chat
    - Response (JSON):
      ```json
      [
        { "contenu": "Hello team", "author": "Nom Prenom", "dateEnvoi": "2025-12-14T10:05:00" },
        { "contenu": "Update done", "author": "Autre Membre", "dateEnvoi": "2025-12-14T10:06:00" }
      ]
      ```

- Users
  - `POST /users/signin` — créer un utilisateur
    - Request (JSON):
      ```json
      { "nom": "Nom", "prenom": "Prenom", "email": "user@example.com", "motDePasse": "secret" }
      ```
    - Response (text): User created successfully
  - `GET /users/projets-joined` — projets rejoints (auth)
    - Response (JSON):
      ```json
      [ { "id": 1, "nom": "Projet A", "description": "Desc", "createurNom": "Nom Prenom", "dateCreation": "2025-12-14T10:00:00", "chatId": 5 } ]
      ```
  - `GET /users/projets-created` — projets créés (auth)
    - Response (JSON):
      ```json
      [ { "id": 2, "nom": "Projet B", "description": "Desc", "createurNom": "Nom Prenom", "dateCreation": "2025-12-15T09:00:00", "chatId": 6 } ]
      ```
  - `PATCH /users/profile` — mettre à jour profil (auth)
    - Request (JSON):
      ```json
      { "nom": "Nom Modifié", "prenom": "Prenom Modifié" }
      ```
    - Response (JSON):
      ```json
      { "nom": "Nom Modifié", "prenom": "Prenom Modifié" }
      ```
  - `PATCH /users/password` — changer mot de passe (auth)
    - Request (JSON):
      ```json
      { "oldPassword": "secret", "newPassword": "newSecret" }
      ```
    - Response (text): Password updated successfully
  - `GET /users/profile` — profil utilisateur (auth)
    - Response (JSON):
      ```json
      { "nom": "Nom", "prenom": "Prenom", "email": "user@example.com" }
      ```
  - `GET /users/taches-assignees` — tâches assignées (auth)
    - Response (JSON):
      ```json
      [ { "id": 101, "titre": "Tâche 1", "etat": "Todo", "deadline": "2025-12-31T23:59:00", "priorite": 2, "projectTitre": "Projet A" } ]
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

## Auteurs
- Malek Toumi — https://github.com/MalekToumi-815
- Malek Chermiti — https://github.com/malek-chermiti