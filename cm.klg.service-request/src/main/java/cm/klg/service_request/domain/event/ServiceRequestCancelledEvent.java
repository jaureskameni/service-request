package cm.klg.service_request.domain.event;

import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.IdentityId;

public record ServiceRequestCancelledEvent(
    ServiceRequestId id,
    IdentityId userId,
    ServiceProviderId providerId,
    ServiceRequestStatus status,
    UpdatedAt updatedAt) {}
