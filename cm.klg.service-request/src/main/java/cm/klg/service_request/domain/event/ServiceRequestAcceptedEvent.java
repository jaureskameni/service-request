package cm.klg.service_request.domain.event;

import cm.klg.service_request.domain.service_request.ServiceRequestDetails;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestLifecycle;
import cm.klg.service_request.domain.service_request.ServiceRequestParties;

public record ServiceRequestAcceptedEvent(
    ServiceRequestId id,
    ServiceRequestParties parties,
    ServiceRequestDetails details,
    ServiceRequestLifecycle lifecycle) {}
