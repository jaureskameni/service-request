package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.review.ReviewComment;
import cm.klg.service_request.domain.review.ReviewId;
import cm.klg.service_request.domain.user.UserId;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HideReviewUseCase {
  private final ReviewRepository reviewRepository;

  public void execute(Command command) {
    var review = reviewRepository.load(command.reviewId());
    review.hide(
        ReviewComment.from(command.reason()),
        command.moderatorId(),
        UpdatedAt.from(LocalDateTime.now()));
    reviewRepository.update(review);
  }

  public record Command(ReviewId reviewId, UserId moderatorId, String reason) {}
}
