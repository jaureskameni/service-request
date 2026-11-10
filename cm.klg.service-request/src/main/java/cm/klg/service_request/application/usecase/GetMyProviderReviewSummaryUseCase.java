package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.views.ReviewSummary;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMyProviderReviewSummaryUseCase {
  private final ReviewRepository reviewRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public ReviewSummary execute(UserId userId) {
    var provider =
        serviceProviderRepository
            .loadApprovedByUserId(userId)
            .orElseThrow(ServiceProviderNotFoundException::new);
    return reviewRepository.summarizePublishedByProvider(provider.getId());
  }
}
