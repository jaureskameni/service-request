# Vue de lecture — `service-request`

Les vues sont les projections de lecture utilisées par les repositories et les cas d'utilisation. L'API expose une
seule projection, `ServiceRequestView1`, définie dans
[`ServiceRequestViews.java`](../../src/main/java/cm/klg/service_request/application/views/ServiceRequestViews.java).
Elle alimente le détail d'une demande ainsi que les listes de demandes de l'utilisateur courant et du prestataire
courant.

## `ServiceRequestView1`

| Champ               | Type Java                | Champ REST      | Signification                                                   |
|---------------------|--------------------------|-----------------|-----------------------------------------------------------------|
| `id`                | `UUID`                   | `id`            | Identifiant de la demande.                                      |
| `userId`            | `UUID`                   | `userId`        | Identifiant de l'utilisateur ayant créé la demande.             |
| `serviceProviderId` | `UUID`                   | `providerId`    | Identifiant du prestataire destinataire.                        |
| `serviceTypeId`     | `UUID`                   | `serviceTypeId` | Identifiant du type de service demandé.                         |
| `title`             | `String` nullable        | `title`         | Titre de la demande, absent si non renseigné.                   |
| `description`       | `String` nullable        | `description`   | Détails de la demande, absents si non renseignés.               |
| `location`          | `String` nullable        | `location`      | Lieu d'intervention, absent si non renseigné.                   |
| `status`            | `String`                 | `status`        | Statut : `PENDING`, `ACCEPTED`, `REJECTED` ou `CANCELLED`.      |
| `reason`            | `String` nullable        | `reason`        | Motif fourni lors du rejet, sinon absent.                       |
| `createdAt`         | `LocalDateTime`          | `createdAt`     | Date de création de la demande.                                 |
| `updatedAt`         | `LocalDateTime` nullable | `updatedAt`     | Date de la dernière transition, absente avant toute transition. |

## Réponses REST associées

- `GET /service-request/{serviceRequestId}` retourne un `ServiceRequestView1` mappé en `ServiceRequest`.
- `GET /my/service-request` et `GET /my/provider/service-requests` retournent chacun une collection de demandes et le
  nombre total correspondant, sous la forme `ServiceRequestPaginate` : `serviceRequest` (liste de `ServiceRequest`) et
  `count`.
- Le DTO REST nomme l'identifiant du prestataire `providerId`, tandis que la projection applicative le nomme
  `serviceProviderId`.

Le contrat des champs REST est défini dans
[`main.yml`](../openapi/inbound/main.yml). Cette vue ne contient aucune information de profil du prestataire ou de
l'utilisateur, ni de données de catalogue; elle ne représente que les champs de demande exposés par le contrat.
