package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.UpdatedAt;

public record ServiceRequestLifecycle(
    ServiceRequestStatus status,
    UpdatedAt updatedAt,
    CreatedAt createdAt,
    ServiceRequestReason reason,
    AcceptedAt acceptedAt) {
  public ServiceRequestLifecycle(
      ServiceRequestStatus status,
      UpdatedAt updatedAt,
      CreatedAt createdAt,
      ServiceRequestReason reason) {
    this(status, updatedAt, createdAt, reason, AcceptedAt.NULL);
  }
}
