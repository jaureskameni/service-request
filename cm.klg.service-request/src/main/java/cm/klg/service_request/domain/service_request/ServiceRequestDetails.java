package cm.klg.service_request.domain.service_request;

public record ServiceRequestDetails(
    ServiceRequestTitle title,
    ServiceRequestDescription description,
    ServiceRequestLocation location) {}
