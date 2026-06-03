package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestDetails;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestParties;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.service_request.ServiceTypeId;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class CreateNewServiceRequestUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final ServiceRequestRepository serviceRequestRepository;

  public ServiceRequestId execute(Command command) {
    ServiceProviderId serviceProviderId = command.serviceProviderId;
    if (!serviceProviderRepository.existsById(serviceProviderId)) {
      throw new ServiceProviderNotFoundException();
    }
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(command.userId, serviceProviderId, command.serviceTypeId),
            new ServiceRequestDetails(
                command.title != null ? command.title : ServiceRequestTitle.NULL,
                command.description != null ? command.description : ServiceRequestDescription.NULL,
                command.location != null ? command.location : ServiceRequestLocation.NULL));
    serviceRequestRepository.insert(serviceRequest);
    return serviceRequest.getId();
  }

  public record Command(
      UserId userId,
      ServiceProviderId serviceProviderId,
      ServiceTypeId serviceTypeId,
      @Nullable ServiceRequestTitle title,
      @Nullable ServiceRequestDescription description,
      @Nullable ServiceRequestLocation location) {}
}
