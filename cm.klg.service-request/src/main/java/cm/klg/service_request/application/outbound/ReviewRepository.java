package cm.klg.service_request.application.outbound;

import cm.klg.service_request.application.views.ReviewSummary;
import cm.klg.service_request.application.views.ReviewViews;
import cm.klg.service_request.domain.review.Review;
import cm.klg.service_request.domain.review.ReviewId;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;

public interface ReviewRepository {
  boolean existsByServiceRequestId(ServiceRequestId serviceRequestId);

  void insert(Review review);

  Review load(ReviewId reviewId);

  void update(Review review);

  PageData<ReviewViews.PublishedReviewView> loadPublishedByProvider(
      ServiceProviderId providerId, PaginationFetchRequest pagination);

  ReviewSummary summarizePublishedByProvider(ServiceProviderId providerId);
}
