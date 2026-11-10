package cm.klg.service_request.domain.event;

import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.FollowUpDateTime;
import cm.klg.service_request.domain.service_request.FollowUpStage;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;

public record ServiceRequestFollowUpRequestedEvent(
    ServiceRequestId serviceRequestId,
    UserId clientId,
    ServiceProviderId providerId,
    FollowUpStage stage,
    FollowUpDateTime requestedAt) {}
