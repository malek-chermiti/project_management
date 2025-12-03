# Project Manager - REST API

## Description
Project Manager est une application backend développée en *Spring Boot* avec approche *Code-First*.  
Elle permet aux utilisateurs de créer ou rejoindre des projets, gérer des tâches, envoyer des messages, et pour les administrateurs de gérer les membres et finaliser les tâches.

Ce projet utilise *Maven* pour la gestion des dépendances et *Spring Data JPA* pour la persistance avec une base de données MySQL.

---

## Fonctionnalités principales

### Utilisateur
- S'inscrire / se connecter / mot de passe oublié
- Créer ou rejoindre un projet
- Gérer les tâches : ajouter, modifier, supprimer
- Changer l'état d'une tâche (sauf terminé)
- Envoyer des messages dans un chat lié à un projet

### Administrateur
- Hérite des fonctionnalités utilisateur
- Terminer une tâche
- Exclure des membres d’un projet

---

## Structure du projet
Voici la structure principale du dépôt (les chemins sont relatifs à la racine du projet) :

mvnw
mvnw.cmd
pom.xml
src/
	main/
		java/
			com/
				example/
					project_management/
						ProjectManagementApplication.java
						model/
							Admin.java
							Chat.java
							Message.java
							Projet.java
							Task.java
							User.java
		resources/
			application.properties
generated-sources/
		annotations/
generated-test-sources/
		test-annotations/
test-classes/

*Fichiers importants :*
- pom.xml : configuration Maven et dépendances
- src/main/java/com/example/project_management/ProjectManagementApplication.java : point d'entrée Spring Boot
- src/main/resources/application.properties : configuration Spring (lecture de variables d'environnement)
- .env (non commité) : variables d'environnement locales (ex. DB_URL, DB_USERNAME, DB_PASSWORD)

*Exécution locale :*
- Construire :

.\\mvnw.cmd clean package

- Lancer l'application :

.\\mvnw.cmd spring-boot:run

L'application utilise la variable spring.datasource.url=${DB_URL} dans application.properties. Assurez-vous que votre fichier .env (ou vos variables d'environnement système) contient DB_URL pointant vers votre instance MySQL (par défaut jdbc:mysql://localhost:3306/project_management_db).

---