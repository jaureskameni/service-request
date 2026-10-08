# API REST du microservice `service-request`

Cette documentation décrit uniquement les routes définies dans [`main.yml`](../openapi/inbound/main.yml).
Le port local `8086` provient de `server.port` dans `src/main/resources/application.yaml`.

Toutes les routes nécessitent un token Bearer JWT. Utiliser le token de l'utilisateur qui effectue l'action :
un utilisateur crée et consulte ses demandes, tandis qu'un prestataire consulte et traite les demandes qui lui
sont destinées.

## Routes

| Méthode et route | Description | Paramètres / corps | Réponses documentées |
|---|---|---|---|
| `POST /service-request` | Créer une demande de service pour un prestataire. | Corps `ServiceRequestRegister` : `providerId` (UUID, requis), `serviceTypeId` (UUID, requis), `title`, `description`, `location` (chaînes optionnelles). | `201 CreationResponse`; `400` corps invalide; `403` prestataire non approuvé (`SERVICE_REQUEST_403_003`); `404` prestataire absent (`SERVICE_REQUEST_404_002`); `401`; `500`. |
| `GET /service-request/{serviceRequestId}` | Lire une demande par son identifiant. | `serviceRequestId` UUID dans le chemin. | `200 ServiceRequest`; `400` UUID invalide; `404` demande absente (`SERVICE_REQUEST_404_003`); `401`; `500`. |
| `PUT /service-request/{serviceRequestId}/accept` | Accepter une demande reçue par le prestataire. | `serviceRequestId` UUID dans le chemin; aucun corps. | `204`; `400` UUID invalide; `403` demande appartenant à un autre prestataire (`SERVICE_REQUEST_403_001`) ou prestataire non approuvé (`SERVICE_REQUEST_403_003`); `404` demande absente (`SERVICE_REQUEST_404_003`); `409` transition invalide (`SERVICE_REQUEST_409_001`); `401`; `500`. |
| `PUT /service-request/{serviceRequestId}/reject` | Rejeter une demande reçue par le prestataire. | `serviceRequestId` UUID dans le chemin; corps `{"reason":"..."}` avec `reason` requis (chaîne). | `204`; `400` UUID/corps invalide; `403` demande appartenant à un autre prestataire (`SERVICE_REQUEST_403_001`) ou prestataire non approuvé (`SERVICE_REQUEST_403_003`); `404` demande absente (`SERVICE_REQUEST_404_003`); `409` transition invalide (`SERVICE_REQUEST_409_001`); `401`; `500`. |
| `PUT /service-request/{serviceRequestId}/cancel` | Annuler une demande créée par l'utilisateur courant. | `serviceRequestId` UUID dans le chemin; aucun corps. | `204`; `400` UUID invalide; `403` demande appartenant à un autre utilisateur (`SERVICE_REQUEST_403_002`); `404` demande absente (`SERVICE_REQUEST_404_003`); `409` transition invalide (`SERVICE_REQUEST_409_001`); `401`; `500`. |
| `GET /my/service-request` | Lister les demandes de l'utilisateur courant. | Query `status` optionnel; `page` optionnel (défaut `0`, minimum `0`); `limit` requis (défaut OpenAPI `10`, entre `10` et `100`). | `200 ServiceRequestPaginate`; `400` pagination invalide (`SERVICE_REQUEST_400_001`) ou paramètres invalides; `401`; `500`. |
| `GET /my/provider/service-requests` | Lister les demandes reçues par le prestataire courant. | Query `status` optionnel; `page` optionnel (défaut `0`, minimum `0`); `limit` requis (défaut OpenAPI `10`, entre `10` et `100`). | `200 ServiceRequestPaginate`; `400` pagination invalide (`SERVICE_REQUEST_400_001`) ou paramètres invalides; `403` prestataire non approuvé (`SERVICE_REQUEST_403_003`); `401`; `500`. |

## Schémas

`ServiceRequestRegister` accepte les champs suivants :

| Champ | Type | Requis |
|---|---|---|
| `providerId` | UUID | Oui |
| `serviceTypeId` | UUID | Oui |
| `title` | Chaîne | Non |
| `description` | Chaîne | Non |
| `location` | Chaîne | Non |

`ServiceRequestReject` est un objet sans propriétés supplémentaires, avec le champ requis `reason` de type chaîne.
La spécification ne définit pas d'énumération de motifs.

`ServiceRequest` comprend `id`, `userId`, `providerId`, `serviceTypeId`, `status` et `createdAt` (requis), ainsi que
`title`, `description`, `location`, `updatedAt` et `reason` (optionnels). Les statuts définis sont `PENDING`,
`ACCEPTED`, `REJECTED` et `CANCELLED`.

`ServiceRequestPaginate` contient `serviceRequest` (tableau de demandes) et `count` (entier 64 bits).
`CreationResponse` contient `newId` (UUID).

Les descriptions et statuts ci-dessus suivent le contrat OpenAPI; les erreurs HTTP sans code métier explicite dans
ce contrat peuvent ne pas contenir de code de service.
