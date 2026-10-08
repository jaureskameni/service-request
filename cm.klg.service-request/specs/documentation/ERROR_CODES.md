# Codes d'erreur — `service-request`

Les codes métier ci-dessous sont définis dans
[
`ServiceRequestErrorCode.java`](../../src/main/java/cm/klg/service_request/domain/exception/ServiceRequestErrorCode.java)
et traduits en erreurs HTTP par
[
`DefaultDomainToHttpExceptionTranslator.java`](../../src/main/java/cm/klg/service_request/adapter/rest/inbound/DefaultDomainToHttpExceptionTranslator.java).
Les routes correspondent au contrat [`main.yml`](../openapi/inbound/main.yml).

|  HTTP | Code                      | Signification                                                                                                               | Opérations concernées                                                                                                                                                                           |
|------:|---------------------------|-----------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `400` | `SERVICE_REQUEST_400_001` | Pagination invalide : `page` doit être positif ou nul et `limit` compris entre 10 et 100.                                   | `GET /my/service-request`, `GET /my/provider/service-requests`                                                                                                                                  |
| `403` | `SERVICE_REQUEST_403_001` | La demande n'appartient pas au prestataire courant.                                                                         | `PUT /service-request/{serviceRequestId}/accept`, `PUT /service-request/{serviceRequestId}/reject`                                                                                              |
| `403` | `SERVICE_REQUEST_403_002` | La demande n'appartient pas à l'utilisateur courant.                                                                        | `PUT /service-request/{serviceRequestId}/cancel`                                                                                                                                                |
| `403` | `SERVICE_REQUEST_403_003` | Le prestataire n'est pas approuvé (`APPROVED`).                                                                             | `POST /service-request`, `GET /my/provider/service-requests`, traitement prestataire des demandes                                                                                               |
| `404` | `SERVICE_REQUEST_404_001` | Utilisateur introuvable.                                                                                                    | Opérations dont la résolution du compte utilisateur échoue                                                                                                                                      |
| `404` | `SERVICE_REQUEST_404_002` | Prestataire introuvable.                                                                                                    | `POST /service-request`                                                                                                                                                                         |
| `404` | `SERVICE_REQUEST_404_003` | Demande introuvable.                                                                                                        | `GET /service-request/{serviceRequestId}`, `PUT /service-request/{serviceRequestId}/accept`, `PUT /service-request/{serviceRequestId}/reject`, `PUT /service-request/{serviceRequestId}/cancel` |
| `409` | `SERVICE_REQUEST_409_001` | Transition impossible depuis le statut courant; seules les demandes `PENDING` peuvent être acceptées, rejetées ou annulées. | `PUT /service-request/{serviceRequestId}/accept`, `PUT /service-request/{serviceRequestId}/reject`, `PUT /service-request/{serviceRequestId}/cancel`                                            |

Les erreurs de liaison ou validation HTTP, par exemple un UUID mal formé, une valeur `status` inconnue ou un corps JSON
invalide, renvoient `400` sans garantir de code métier. Un token absent ou invalide renvoie `401`, et un refus
d'autorisation Spring Security renvoie `403`; ces erreurs ne garantissent pas non plus de code métier.
