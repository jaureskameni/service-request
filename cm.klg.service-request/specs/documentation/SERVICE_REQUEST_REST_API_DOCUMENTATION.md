# API REST du microservice `service-request`

Cette documentation résume le contrat défini dans [`main.yml`](../openapi/inbound/main.yml) et les règles d’accès de [`SecurityConfig.java`](../../src/main/java/cm/klg/service_request/config/SecurityConfig.java). En local, l’API écoute sur `http://localhost:8086`.

## Authentification et autorisations

- Les routes marquées **JWT** nécessitent un Bearer JWT valide.
- Les routes marquées **Publique** acceptent les appels sans token.
- Les demandes peuvent être consultées par leur client ou par le prestataire approuvé auquel elles sont assignées. Les autres utilisateurs authentifiés reçoivent `403 SERVICE_REQUEST_403_005`.
- Le masquage d’un avis nécessite le scope `review:hide`, attribué à l’administrateur dans Keycloak.

## Routes

| Méthode et route | Accès | Description et paramètres | Réponses principales |
|---|---|---|---|
| `POST /service-request` | JWT utilisateur | Crée une demande. Corps `ServiceRequestRegister` : `providerId` et `serviceTypeId` (UUID requis), `title`, `description`, `location` (chaînes optionnelles). | `201 CreationResponse`; `403 SERVICE_REQUEST_403_003` prestataire non approuvé; `404 SERVICE_REQUEST_404_002` prestataire introuvable. |
| `GET /service-request/{serviceRequestId}` | JWT, client ou prestataire assigné | Lit le détail d’une demande. `serviceRequestId` est un UUID. | `200 ServiceRequest`; `403 SERVICE_REQUEST_403_005` appelant non associé; `404 SERVICE_REQUEST_404_003` demande absente. |
| `PUT /service-request/{serviceRequestId}/accept` | JWT prestataire | Accepte une demande reçue. Aucun corps. | `204`; `403 SERVICE_REQUEST_403_001` demande assignée à un autre prestataire ou `SERVICE_REQUEST_403_003` prestataire non approuvé; `404 SERVICE_REQUEST_404_003`; `409 SERVICE_REQUEST_409_001`. |
| `PUT /service-request/{serviceRequestId}/reject` | JWT prestataire | Rejette une demande reçue. Corps `{"reason":"..."}`. | `204`; `403 SERVICE_REQUEST_403_001` ou `SERVICE_REQUEST_403_003`; `404 SERVICE_REQUEST_404_003`; `409 SERVICE_REQUEST_409_001`. |
| `PUT /service-request/{serviceRequestId}/cancel` | JWT client | Annule une demande appartenant au client connecté. Aucun corps. | `204`; `403 SERVICE_REQUEST_403_002`; `404 SERVICE_REQUEST_404_003`; `409 SERVICE_REQUEST_409_001`. |
| `GET /my/service-request` | JWT client | Liste ses demandes. Query : `status` optionnel; `page` (défaut `0`, minimum `0`); `limit` (défaut `10`, de `10` à `100`). | `200 ServiceRequestPaginate`; `400 SERVICE_REQUEST_400_001`. |
| `GET /my/provider/service-requests` | JWT prestataire approuvé | Liste les demandes reçues. Mêmes paramètres de pagination que ci-dessus. | `200 ServiceRequestPaginate`; `400 SERVICE_REQUEST_400_001`; `403 SERVICE_REQUEST_403_003`. |
| `GET /service-request/{serviceRequestId}/follow-up` | JWT client ou prestataire associé | Lit l’état bilatéral du suivi et les actions disponibles. | `200 ServiceRequestFollowUp`; `403 SERVICE_REQUEST_403_004` ou `SERVICE_REQUEST_403_005`; `404 SERVICE_REQUEST_404_003` ou `SERVICE_REQUEST_404_004`; `409 SERVICE_REQUEST_409_001`. |
| `PUT /service-request/{serviceRequestId}/follow-up/agreement` | JWT client ou prestataire associé | Enregistre la réponse d’accord. Corps `{"answer":"YES"}` ou `{"answer":"NO"}`. | `204`; `403 SERVICE_REQUEST_403_004` ou `SERVICE_REQUEST_403_005`; `404 SERVICE_REQUEST_404_003` ou `SERVICE_REQUEST_404_004`; `409 SERVICE_REQUEST_409_001` ou `SERVICE_REQUEST_409_002`. |
| `PUT /service-request/{serviceRequestId}/follow-up/service-completion` | JWT client ou prestataire associé | Déclare si le service est terminé. Même corps que pour l’accord. | `204`; `403 SERVICE_REQUEST_403_004` ou `SERVICE_REQUEST_403_005`; `404 SERVICE_REQUEST_404_003`; `409 SERVICE_REQUEST_409_001`, `SERVICE_REQUEST_409_002` ou `SERVICE_REQUEST_409_003`. |
| `POST /service-request/{serviceRequestId}/review` | JWT client | Publie un avis si le client est éligible. Corps `ReviewRequest`. | `204`; `400 SERVICE_REQUEST_400_002`; `403 SERVICE_REQUEST_403_002`; `404 SERVICE_REQUEST_404_003` ou `SERVICE_REQUEST_404_004`; `409 SERVICE_REQUEST_409_004` ou `SERVICE_REQUEST_409_005`. |
| `GET /service-provider/{providerId}/reviews` | Publique | Liste les avis publics. `providerId` UUID; `limit` optionnel (défaut `10`, de `10` à `100`); `page` optionnel (défaut `0`, minimum `0`). | `200 ProviderReviews`; `400 SERVICE_REQUEST_400_001`; `404 SERVICE_REQUEST_404_002`. |
| `GET /service-provider/{providerId}/reviews/summary` | Publique | Renvoie la note moyenne, le nombre d’avis et leur distribution. `providerId` UUID. | `200 ReviewSummary`; `404 SERVICE_REQUEST_404_002`. |
| `GET /me/reviews/summary` | JWT prestataire approuvé | Renvoie les statistiques publiques des avis du prestataire connecté. | `200 ReviewSummary`; `404 SERVICE_REQUEST_404_002` si aucun prestataire approuvé n’est associé au compte. |
| `PUT /reviews/{reviewId}/moderation` | JWT avec le scope `review:hide` (administrateur) | Masque un avis. Corps `ReviewModerationRequest` : `reason` requis, longueur de 1 à 2000 caractères. | `204`; `400 SERVICE_REQUEST_400_003`; `404 SERVICE_REQUEST_404_005`. |

## Schémas principaux

### Demandes

`ServiceRequest` contient les identifiants `id`, `userId`, `providerId` et `serviceTypeId`, son `status`, sa date `createdAt`, ainsi que les champs optionnels `title`, `description`, `location`, `updatedAt` et `reason`. Les statuts sont `PENDING`, `ACCEPTED`, `REJECTED` et `CANCELLED`.

`ServiceRequestPaginate` contient `serviceRequest` (tableau) et `count` (nombre total d’éléments). La création renvoie `CreationResponse` avec `newId`.

### Suivi

`FollowUpAnswerRequest.answer` vaut `YES` ou `NO`. `ServiceRequestFollowUp` contient les réponses et dates du client et du prestataire, `scheduledAt`, `agreementConfirmedAt`, le statut, ainsi que `canSubmitReview`, `canAnswerAgreement` et `canAnswerCompletion`. Les réponses et dates peuvent être `null`.

Les statuts de suivi sont `WAITING_FOR_AGREEMENT`, `AGREEMENT_DISPUTED`, `NO_AGREEMENT`, `WAITING_FOR_COMPLETION`, `COMPLETION_REPORTED_BY_CLIENT`, `COMPLETION_CONFIRMED`, `COMPLETION_DISPUTED` et `NOT_COMPLETED`.

`completionAvailableAt` indique quand la déclaration de fin devient disponible. Après accord des deux parties, cette date est `scheduledAt` si elle est définie; sinon, elle correspond à `agreementConfirmedAt` plus le délai configuré.

### Avis

`ReviewRequest.rating` est un entier de 1 à 5; `comment` est optionnel et limité à 2000 caractères. Un avis public fournit une confirmation parmi `CONFIRMED_BY_BOTH`, `REPORTED_BY_CLIENT` et `CONTESTED_BY_PROVIDER`.

`ReviewSummary` contient `averageRating`, `reviewCount` et les nombres d’avis de une à cinq étoiles.

## Erreurs hors codes métier

Les codes `SERVICE_REQUEST_*` sont les erreurs métier listées dans [`ERROR_CODES.md`](ERROR_CODES.md). Les erreurs de sécurité ou de transport qui ne correspondent pas à une exception métier — par exemple un token absent ou un JSON mal formé — peuvent utiliser le format d’erreur commun sans code `SERVICE_REQUEST_*`.
