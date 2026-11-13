package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_request.ProviderIsNotInvolvedException;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceRequestByIdUseCase {
  private final ServiceRequestRepository serviceRequestRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public ServiceRequest execute(Command command) {
    ServiceRequest serviceRequest = serviceRequestRepository.load(command.id());
    if (serviceRequest.isClient(command.userId())) {
      return serviceRequest;
    }

    ServiceProvider serviceProvider =
        serviceProviderRepository
            .loadApprovedByUserId(command.userId())
            .orElseThrow(ProviderIsNotInvolvedException::new);
    if (!serviceRequest.isAssignedTo(serviceProvider.getId())) {
      throw new ProviderIsNotInvolvedException();
    }
    return serviceRequest;
  }

  public record Command(ServiceRequestId id, UserId userId) {}
}
