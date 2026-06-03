package cm.klg.service_request.domain.service_request;

import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;

public record ServiceRequestParties(
    UserId userId, ServiceProviderId serviceProviderId, ServiceTypeId serviceTypeId) {}
