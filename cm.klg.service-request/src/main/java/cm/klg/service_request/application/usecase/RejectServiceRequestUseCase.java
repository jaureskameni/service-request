package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestReason;
import cm.klg.service_request.domain.user.IdentityId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RejectServiceRequestUseCase {
  private final ServiceProviderRepository serviceProviderRepository;
  private final ServiceRequestRepository serviceRequestRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(Command command) {
    ServiceProvider serviceProvider = serviceProviderRepository.loadByUserId(command.userId);

    ServiceRequest serviceRequest = serviceRequestRepository.load(command.serviceRequestId());

    var event = serviceRequest.reject(serviceProvider.getId(), command.reason());

    serviceRequestRepository.update(serviceRequest);

    domainEventPublisher.publishServiceRequestRejectedEvent(event);
  }

  public record Command(
      IdentityId userId, ServiceRequestId serviceRequestId, ServiceRequestReason reason) {}
}
