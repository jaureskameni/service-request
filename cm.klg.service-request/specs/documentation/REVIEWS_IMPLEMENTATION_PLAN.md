# Plan d’implémentation du suivi et des Reviews

## Résumé

Implémenter dans `service-request` le suivi bilatéral de l’accord et de la réalisation, le dépôt d’avis dès que le client confirme la réalisation, puis l’affichage public des avis et des statistiques du prestataire. Le niveau de confirmation dépend des réponses reçues ; une contestation seule ne masque pas l’avis.

Le dépôt contient déjà une ébauche non persistée des objets métier `ServiceRequestFollowUp` et `Review`. L’implémentation devra la compléter et l’aligner sur les règles ci-dessous.

## Changements prévus

### Suivi et règles métier

- Créer le suivi à l’acceptation de la demande.
- Chaque partie répond en son nom.
- Les deux réponses Oui à l’accord ouvrent l’étape de réalisation ; toute autre combinaison empêche de passer à cette étape.
- La réalisation devient demandable à la date prévue, si elle existe, ou après un délai configurable depuis l’accord mutuel sinon. Le défaut est `PT48H`.
- Les réponses restent corrigeables jusqu’à la clôture de leur étape, lorsque les deux parties ont répondu.

### Configuration et valeurs absentes

- Définir le délai dans `application.yml` sous `service-request.reviews.completion-delay`, au format de durée ISO-8601 (`PT48H` par défaut), et le lier à une configuration Spring typée.
- Représenter dans le domaine les champs optionnels par des sentinelles non nulles : les objets valeur textuels exposent `NULL` et `from(@Nullable String)`, les enums optionnels exposent `NULL`, et les dates/heures optionnelles utilisent un objet valeur `NULL` dédié.
- Appliquer cette convention aussi aux champs optionnels déjà présents sur `ServiceRequest` : titre, description, lieu et motif.
- Convertir les sentinelles en SQL `NULL` et les SQL `NULL` en sentinelles uniquement dans les adaptateurs de persistance.
- Les entités métier portent des Value Objects pour leurs identifiants, notes et dates plutôt que des UUID, chaînes, nombres ou dates bruts.
- Chaque entité JPA représentant une racine d’agrégat étend `AggregateRootEntity<UUID>`, implémente `equals`/`hashCode` à partir de son identifiant, et sa table possède les colonnes héritées `c_deleted` et `c_version`.
- `ServiceRequestFollowUp` est un enfant de l’agrégat `ServiceRequest`, pas un agrégat autonome. Son identifiant métier `ServiceRequestFollowUpId` est persisté dans `c_id`, et sa ligne JPA est reliée en 1-à-1 à la demande via `c_service_request_id` unique. Les transitions passent par la racine `ServiceRequest`.

### Avis

- Une réponse Oui du client sur la réalisation autorise immédiatement son avis, même sans réponse ou en cas de réponse Non du prestataire.
- Une contrainte en base garantit un avis par demande et client.
- Une contestation ne masque pas l’avis.
- Un rôle Keycloak `review-moderate` permet à un modérateur de masquer un avis avec motif et trace de modération.

### Persistance et API

- Ajouter les tables de suivi et d’avis avec une migration Liquibase, les use cases, les adaptateurs JPA et les opérations OpenAPI.
- Prévoir `GET` et les réponses d’accord, date prévue et réalisation dans le suivi.
- Prévoir `POST` pour publier l’avis.
- Prévoir les lectures publiques paginées des avis et du résumé (moyenne, nombre, distribution).
- Prévoir une opération de modération protégée.
- Les routes de suivi exigent l’authentification et vérifient que l’appelant est l’une des deux parties. La consultation du profil est publique.

### Notifications

- Publier les événements d’invitation via l’outbox existante : demande d’accord à l’acceptation, demande de confirmation de réalisation à l’échéance, et publication d’avis.
- Un scheduler détecte l’échéance et marque l’événement comme émis de façon idempotente.
- La livraison des notifications reste au service consommateur.

### Niveau de confirmation public

- Dériver l’indicateur des réponses courantes : confirmé par les deux, déclaré par le client sans réponse, ou contesté par le prestataire.
- Une réponse ultérieure du prestataire actualise cet indicateur.
- Seuls les avis publiés et non masqués entrent dans les avis et statistiques publics.

## Vérifications à prévoir

- Transitions métier et autorisation des deux parties.
- Corrections permises avant clôture de l’étape.
- Ouverture de la question de réalisation à la bonne échéance.
- Éligibilité à l’avis et unicité par demande.
- Réponses absentes ou contradictoires.
- Niveaux de confirmation, modération et exclusion des avis masqués des statistiques.
- Absence d’invitations en double par le scheduler et l’outbox.

## Hypothèses retenues pour le MVP

- Une date prévue fixe l’échéance ; sans date, le délai de `service-request.reviews.completion-delay` est calculé depuis la confirmation mutuelle de l’accord. Le défaut est `PT48H`.
- Une étape se clôt lorsque les deux parties ont répondu. Aucune réponse manquante n’est convertie en Non et aucun délai d’expiration automatique n’est ajouté dans ce MVP.
- Le commentaire est facultatif ; la note est requise et comprise entre 1 et 5.
- L’avis est publié immédiatement après sa création et n’est masqué que par la modération.
- La modification d’un avis publié et les rappels répétés ne font pas partie de ce MVP.
- Les propriétés du domaine ne contiennent pas de référence Java `null` pour les champs optionnels ; seuls les adaptateurs REST/JPA traduisent entre les valeurs externes nulles et les sentinelles.

## Découpage de l’implémentation

Chaque tâche est développée sur une branche dédiée, commitée avant le début de la suivante. Les branches de code sont empilées sur la branche de la tâche précédente pour permettre une revue séparée.

1. `working/SR-25` : documentation du flow et du plan d’implémentation.
2. `working/SR-26` : modèle métier, sentinelles et délai configurable.
3. `working/SR-27` : migration, persistance et repositories.
4. `working/SR-28` : alignement des agrégats métier et entités JPA sur les conventions Value Object et `AggregateRootEntity`.
5. `working/SR-29` : harmonisation du nom de clé `id` dans l’entité JPA de suivi.
6. `working/SR-30` : API du suivi, événements et échéances planifiées.
7. `working/SR-31` : publication, consultation publique et modération des avis.
8. `working/SR-32` : intégrer le suivi comme enfant de l’agrégat `ServiceRequest` et retirer son repository autonome.
9. `working/SR-33` : simplifier le modèle métier du suivi.
10. `working/SR-34` : donner au suivi un identifiant JPA propre sans en faire une racine d’agrégat.
11. `working/SR-35` : aligner l’identifiant métier du suivi avec son identifiant JPA.

Les messages de commit sont en anglais et décrivent explicitement la tâche.
