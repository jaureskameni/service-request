package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;

public record Parties(
    ServiceRequestId serviceRequestId, UserId clientId, ServiceProviderId providerId) {}
