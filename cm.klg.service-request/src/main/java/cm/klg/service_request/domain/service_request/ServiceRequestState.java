package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;

public record ServiceRequestState(ServiceRequestStatus status, CreatedAt createdAt) {}
