package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.views.ReviewViews;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.utils.PaginationFetchRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetPublishedProviderReviewsUseCase {
  private final ReviewRepository reviewRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public Response getPublishedReviews(Command command) {
    requireApprovedProvider(command.providerId());
    var page = reviewRepository.loadPublishedByProvider(command.providerId(), command.pagination());
    return new Response(page.total(), page.elements());
  }

  private void requireApprovedProvider(ServiceProviderId providerId) {
    if (!serviceProviderRepository.isApprovedById(providerId)) {
      throw new ServiceProviderNotFoundException();
    }
  }

  public record Command(ServiceProviderId providerId, PaginationFetchRequest pagination) {}

  public record Response(long count, List<ReviewViews.PublishedReviewView> reviews) {}
}
