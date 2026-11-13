# Codes d’erreur — `service-request`

Les codes sont définis dans [`ServiceRequestErrorCode.java`](../../src/main/java/cm/klg/service_request/domain/exception/ServiceRequestErrorCode.java).
Les exceptions métier correspondantes sont traduites en statuts HTTP par
[`DefaultDomainToHttpExceptionTranslator.java`](../../src/main/java/cm/klg/service_request/adapter/rest/inbound/DefaultDomainToHttpExceptionTranslator.java).
Les opérations HTTP indiquées ci-dessous correspondent au contrat
[`main.yml`](../openapi/inbound/main.yml). Une opération peut produire plusieurs codes du même statut.

| HTTP | Code | Signification | Opérations concernées |
|---:|---|---|---|
| `400` | `SERVICE_REQUEST_400_001` | Données de pagination invalides (`page` négatif ou `limit` hors de 10 à 100). | `GET /my/service-request`, `GET /my/provider/service-requests` |
| `400` | `SERVICE_REQUEST_400_002` | Données d’avis invalides (note ou commentaire). | `POST /service-request/{serviceRequestId}/review` |
| `400` | `SERVICE_REQUEST_400_003` | Données de modération d’avis invalides (motif absent, vide ou trop long). | `PUT /reviews/{reviewId}/moderation` |
| `403` | `SERVICE_REQUEST_403_001` | La demande n’appartient pas au prestataire courant. | `PUT /service-request/{serviceRequestId}/accept`, `PUT /service-request/{serviceRequestId}/reject` |
| `403` | `SERVICE_REQUEST_403_002` | La demande n’appartient pas à l’utilisateur courant. | `PUT /service-request/{serviceRequestId}/cancel`, `POST /service-request/{serviceRequestId}/review` |
| `403` | `SERVICE_REQUEST_403_003` | Le prestataire n’est pas autorisé, notamment parce qu’il n’est pas approuvé. | `POST /service-request`, `GET /my/provider/service-requests`, opérations prestataire `accept` et `reject` |
| `403` | `SERVICE_REQUEST_403_004` | L’utilisateur courant ne participe pas à la demande. | `GET /service-request/{serviceRequestId}/follow-up` et opérations `agreement`, `service-completion` |
| `403` | `SERVICE_REQUEST_403_005` | Le prestataire courant ne participe pas à la demande. | `GET /service-request/{serviceRequestId}`, `GET /service-request/{serviceRequestId}/follow-up` et opérations `agreement`, `service-completion` |
| `404` | `SERVICE_REQUEST_404_001` | Utilisateur introuvable. | Résolution d’un utilisateur dans les opérations applicatives utilisateur (hors routes de `main.yml`) |
| `404` | `SERVICE_REQUEST_404_002` | Prestataire introuvable ou non public. | `POST /service-request`, `GET /service-provider/{providerId}/reviews`, `GET /service-provider/{providerId}/reviews/summary` |
| `404` | `SERVICE_REQUEST_404_003` | Demande introuvable. | Opérations de demande et de suivi sous `/service-request/{serviceRequestId}` |
| `404` | `SERVICE_REQUEST_404_004` | Suivi de la demande introuvable. | `GET /service-request/{serviceRequestId}/follow-up`, `PUT /service-request/{serviceRequestId}/follow-up/agreement`, `POST /service-request/{serviceRequestId}/review` |
| `404` | `SERVICE_REQUEST_404_005` | Avis introuvable. | `PUT /reviews/{reviewId}/moderation` |
| `409` | `SERVICE_REQUEST_409_001` | Statut de demande incompatible avec l’opération. | `accept`, `reject`, `cancel`, `GET follow-up`, `agreement` et `service-completion` |
| `409` | `SERVICE_REQUEST_409_002` | Transition de suivi invalide. | `PUT /service-request/{serviceRequestId}/follow-up/agreement` et `service-completion` |
| `409` | `SERVICE_REQUEST_409_003` | La confirmation de fin de service n’est pas encore disponible. | `PUT /service-request/{serviceRequestId}/follow-up/service-completion` |
| `409` | `SERVICE_REQUEST_409_004` | Le client n’est pas encore éligible à publier un avis. | `POST /service-request/{serviceRequestId}/review` |
| `409` | `SERVICE_REQUEST_409_005` | Un avis existe déjà pour cette demande. | `POST /service-request/{serviceRequestId}/review` |

Les erreurs de transport ou de sécurité qui ne correspondent pas à une exception métier — par exemple un corps JSON invalide, un UUID mal formé ou un token absent — ne portent pas de code `ServiceRequestErrorCode`.
