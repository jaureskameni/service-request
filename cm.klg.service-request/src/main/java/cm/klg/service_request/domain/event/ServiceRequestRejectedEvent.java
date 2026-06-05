package cm.klg.service_request.domain.event;

import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestReason;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.UserId;
import org.jspecify.annotations.Nullable;

public record ServiceRequestRejectedEvent(
    ServiceRequestId id,
    UserId userId,
    ServiceProviderId providerId,
    ServiceRequestStatus status,
    UpdatedAt updatedAt,
    @Nullable ServiceRequestReason reason) {}
