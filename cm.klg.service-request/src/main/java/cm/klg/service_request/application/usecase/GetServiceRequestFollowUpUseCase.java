package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.config.Configurations;
import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestFollowUpActions;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceRequestFollowUpUseCase {
  private final ServiceRequestRepository serviceRequestRepository;
  private final ServiceProviderRepository serviceProviderRepository;
  private final ReviewRepository reviewRepository;
  private final Configurations configurations;

  public Response execute(Command command) {
    ServiceRequest serviceRequest = serviceRequestRepository.load(command.serviceRequestId());
    boolean reviewAlreadyExists =
        reviewRepository.existsByServiceRequestId(command.serviceRequestId());
    ServiceRequestFollowUpActions actions =
        serviceRequest.availableActionsFor(
            command.userId(),
            providerIdFor(command.userId()),
            configurations.getFollowUpDelay(),
            reviewAlreadyExists);
    return new Response(serviceRequest, actions);
  }

  private ServiceProviderId providerIdFor(UserId userId) {
    try {
      return serviceProviderRepository.loadByUserId(userId).getId();
    } catch (ServiceProviderNotFoundException exception) {
      return ServiceProviderId.NULL;
    }
  }

  public record Command(ServiceRequestId serviceRequestId, UserId userId) {}

  public record Response(ServiceRequest serviceRequest, ServiceRequestFollowUpActions actions) {}
}
