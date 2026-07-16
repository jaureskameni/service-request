package cm.klg.service_request.domain.service_request;

import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.IdentityId;

public record ServiceRequestParties(
    IdentityId userId, ServiceProviderId serviceProviderId, ServiceTypeId serviceTypeId) {}
