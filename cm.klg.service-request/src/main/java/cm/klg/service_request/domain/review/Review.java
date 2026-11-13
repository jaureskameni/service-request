package cm.klg.service_request.domain.review;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Review {
  private final ReviewId id;
  private final ServiceRequestId serviceRequestId;
  private final UserId clientId;
  private final ServiceProviderId providerId;
  private final ReviewRating rating;
  private final ReviewComment comment;
  private ReviewStatus status;
  private final CreatedAt createdAt;
  private ReviewComment moderationReason;
  private UserId moderatedBy;
  private UpdatedAt moderatedAt;

  public Review(
      ReviewId id, Parties parties, Content content, Lifecycle lifecycle, Moderation moderation) {
    this.id = id;
    this.serviceRequestId = parties.serviceRequestId();
    this.clientId = parties.clientId();
    this.providerId = parties.providerId();
    this.rating = content.rating();
    this.comment = content.comment();
    this.status = lifecycle.status();
    this.createdAt = lifecycle.createdAt();
    this.moderationReason = moderation.reason();
    this.moderatedBy = moderation.moderatorId();
    this.moderatedAt = moderation.moderatedAt();
  }

  public static Review of(
      ServiceRequestId serviceRequestId,
      UserId clientId,
      ServiceProviderId providerId,
      ReviewRating rating,
      ReviewComment comment) {
    if (comment != ReviewComment.NULL && comment.value().length() > 2000) {
      throw new InvalidReviewException();
    }
    return new Review(
        ReviewId.generate(),
        new Parties(serviceRequestId, clientId, providerId),
        new Content(rating, comment),
        new Lifecycle(ReviewStatus.PUBLISHED, new CreatedAt(LocalDateTime.now())),
        Moderation.NONE);
  }

  public void hide(ReviewComment reason, UserId moderatorId, UpdatedAt moderatedAt) {
    if (reason == ReviewComment.NULL
        || reason.value().isBlank()
        || reason.value().length() > 2000) {
      throw new InvalidReviewModerationException();
    }
    this.status = ReviewStatus.HIDDEN;
    this.moderationReason = reason;
    this.moderatedBy = moderatorId;
    this.moderatedAt = moderatedAt;
  }
}
