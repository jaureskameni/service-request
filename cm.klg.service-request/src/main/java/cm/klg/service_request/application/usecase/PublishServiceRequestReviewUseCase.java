package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.review.ReviewAlreadyExistsException;
import cm.klg.service_request.domain.review.ReviewComment;
import cm.klg.service_request.domain.review.ReviewRating;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PublishServiceRequestReviewUseCase {
  private final ServiceRequestRepository serviceRequestRepository;
  private final ReviewRepository reviewRepository;

  public void execute(Command command) {
    var serviceRequest = serviceRequestRepository.load(command.serviceRequestId());
    if (reviewRepository.existsByServiceRequestId(command.serviceRequestId())) {
      throw new ReviewAlreadyExistsException();
    }
    var review =
        serviceRequest.publishReview(command.clientId(), command.rating(), command.comment());

    reviewRepository.insert(review);
  }

  public record Command(
      ServiceRequestId serviceRequestId,
      UserId clientId,
      ReviewRating rating,
      ReviewComment comment) {}
}
