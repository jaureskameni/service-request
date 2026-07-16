package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.IdentityId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelServiceRequestUseCase {
  private final ServiceRequestRepository serviceRequestRepository;
  private final DomainEventPublisher domainEventPublisher;

  public void execute(Command command) {
    ServiceRequest serviceRequest = serviceRequestRepository.load(command.serviceRequestId());

    var event = serviceRequest.cancel(command.userId());

    serviceRequestRepository.update(serviceRequest);

    domainEventPublisher.publishServiceRequestCancelledEvent(event);
  }

  public record Command(IdentityId userId, ServiceRequestId serviceRequestId) {}
}
