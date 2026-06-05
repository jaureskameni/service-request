package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.UpdatedAt;
import org.jspecify.annotations.Nullable;

public record ServiceRequestLifecycle(
    ServiceRequestStatus status,
    @Nullable UpdatedAt updatedAt,
    CreatedAt createdAt,
    @Nullable ServiceRequestReason reason) {}
