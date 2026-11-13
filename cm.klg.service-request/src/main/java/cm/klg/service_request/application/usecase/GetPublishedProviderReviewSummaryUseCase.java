package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.views.ReviewSummary;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetPublishedProviderReviewSummaryUseCase {
  private final ReviewRepository reviewRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public ReviewSummary execute(ServiceProviderId providerId) {
    if (!serviceProviderRepository.isApprovedById(providerId)) {
      throw new ServiceProviderNotFoundException();
    }
    return reviewRepository.summarizePublishedByProvider(providerId);
  }
}
