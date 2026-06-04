package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceRequestByIdUseCase {
  private final ServiceRequestRepository serviceRequestRepository;

  public ServiceRequestView1 execute(Command command) {
    return serviceRequestRepository.loadByIdAsView1(command.id());
  }

  public record Command(ServiceRequestId id) {}
}
