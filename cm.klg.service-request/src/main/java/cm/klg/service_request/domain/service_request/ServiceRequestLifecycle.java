package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import org.jspecify.annotations.Nullable;

public record ServiceRequestLifecycle(
    ServiceRequestStatus status, @Nullable CreatedAt acceptAt, CreatedAt createdAt) {}
