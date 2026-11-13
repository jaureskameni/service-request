package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.views.ReviewSummary;
import cm.klg.service_request.application.views.ReviewViews.PublishedReviewView;
import cm.klg.service_request.domain.review.Review;
import cm.klg.service_request.domain.review.ReviewId;
import cm.klg.service_request.domain.review.ReviewNotFoundException;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class ReviewJpaRepository implements ReviewRepository {
  private final ReviewSpringRepository springRepository;
  private final JpaMapper jpaMapper;

  @Override
  public boolean existsByServiceRequestId(@NonNull ServiceRequestId serviceRequestId) {
    return springRepository.existsByServiceRequestId(serviceRequestId.value());
  }

  @Override
  public void insert(@NonNull Review review) {
    springRepository.save(jpaMapper.toReviewJpa(review));
  }

  @Override
  public Review load(@NonNull ReviewId reviewId) {
    return springRepository
        .findById(reviewId.value())
        .map(jpaMapper::toReviewSummary)
        .orElseThrow(ReviewNotFoundException::new);
  }

  @Override
  public void update(@NonNull Review review) {
    springRepository
        .findById(review.getId().value())
        .ifPresentOrElse(
            jpa -> {
              jpaMapper.updateReviewJpa(jpa, review);
              springRepository.save(jpa);
            },
            () -> {
              throw new ReviewNotFoundException();
            });
  }

  @Override
  public PageData<@NonNull PublishedReviewView> loadPublishedByProvider(
      @NonNull ServiceProviderId providerId, @NonNull PaginationFetchRequest pagination) {
    var page =
        springRepository.findPublishedViewsByProviderId(
            providerId.value(), PageRequest.of(pagination.pageIndex(), pagination.limit()));
    return new PageData<>(page.getTotalElements(), page.getContent());
  }

  @Override
  public ReviewSummary summarizePublishedByProvider(@NonNull ServiceProviderId providerId) {
    return springRepository
        .summarizePublishedReviews(providerId.value())
        .map(jpaMapper::toReviewSummary)
        .orElse(new ReviewSummary(0.0, 0, 0, 0, 0, 0, 0));
  }
}
