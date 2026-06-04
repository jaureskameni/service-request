package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestDetails(
    @Nullable ServiceRequestTitle title,
    @Nullable ServiceRequestDescription description,
    @Nullable ServiceRequestLocation location) {}
