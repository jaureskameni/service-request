package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.views.ReviewRatingSummary;
import cm.klg.service_request.application.views.ReviewViews.PublishedReviewView;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewSpringRepository extends JpaRepository<@NonNull ReviewJpa, @NonNull UUID> {
  boolean existsByServiceRequestId(UUID serviceRequestId);

  @Query(
      value =
          """
           SELECT\s
                     review.id               AS id,
                     review.serviceRequestId AS serviceRequestId,
                     review.clientId         AS clientId,
                     review.rating           AS rating,
                     review.comment          AS comment,
                     CASE
                       WHEN followUp.providerCompletion = 'YES' THEN 'CONFIRMED_BY_BOTH'
                       WHEN followUp.providerCompletion = 'NO' THEN 'CONTESTED_BY_PROVIDER'
                       ELSE 'REPORTED_BY_CLIENT'
                     END                     AS confirmation,
                     review.createdAt        AS createdAt
           FROM ReviewJpa review
           LEFT JOIN ServiceRequestFollowUpJpa followUp
             ON followUp.serviceRequest.id = review.serviceRequestId
           WHERE review.providerId = :providerId AND review.status = 'PUBLISHED'
           ORDER BY review.createdAt DESC, review.id DESC
           \
          """,
      countQuery =
          "SELECT COUNT(review.id) FROM ReviewJpa review "
              + "WHERE review.providerId = :providerId AND review.status = 'PUBLISHED'")
  Page<@NonNull PublishedReviewView> findPublishedViewsByProviderId(
      @Param("providerId") UUID providerId, Pageable pageable);

  @Query(
      """
      SELECT AVG(review.rating) AS averageRating,
        COUNT(review.id) AS reviewCount,
        SUM(CASE WHEN review.rating = 1 THEN 1 ELSE 0 END) AS oneStarCount,
        SUM(CASE WHEN review.rating = 2 THEN 1 ELSE 0 END) AS twoStarCount,
        SUM(CASE WHEN review.rating = 3 THEN 1 ELSE 0 END) AS threeStarCount,
        SUM(CASE WHEN review.rating = 4 THEN 1 ELSE 0 END) AS fourStarCount,
        SUM(CASE WHEN review.rating = 5 THEN 1 ELSE 0 END) AS fiveStarCount
      FROM ReviewJpa review
      WHERE review.providerId = :providerId AND review.status = 'PUBLISHED'
      """)
  Optional<@NonNull ReviewRatingSummary> summarizePublishedReviews(
      @Param("providerId") UUID providerId);
}
