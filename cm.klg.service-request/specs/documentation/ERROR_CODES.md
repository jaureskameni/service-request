# Error Codes — Service Request

Business errors are returned with their stable code in the HTTP response.

| Type | HTTP | Code métier | Description |
|---|---:|---|---|
| `ForbiddenException` | `403` | `SERVICE_REQUEST_403_001` | The request does not belong to the current service provider. |
| `ForbiddenException` | `403` | `SERVICE_REQUEST_403_002` | The request does not belong to the current user. |
| `ForbiddenException` | `403` | `SERVICE_REQUEST_403_003` | The service provider is not approved (`APPROVED`). |
| `ResourceNotFoundException` | `404` | `SERVICE_REQUEST_404_001` | User not found. |
| `ResourceNotFoundException` | `404` | `SERVICE_REQUEST_404_002` | Service provider not found. |
| `ResourceNotFoundException` | `404` | `SERVICE_REQUEST_404_003` | Service request not found. |
| `ConflictException` | `409` | `SERVICE_REQUEST_409_001` | The transition is invalid for the current status. |

The codes are defined in `ServiceRequestErrorCode`; clients should use the code instead of the
descriptive text.
