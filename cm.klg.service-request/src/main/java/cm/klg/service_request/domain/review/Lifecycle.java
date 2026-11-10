package cm.klg.service_request.domain.review;

import cm.klg.common.base.domain.CreatedAt;

public record Lifecycle(ReviewStatus status, CreatedAt createdAt) {}
