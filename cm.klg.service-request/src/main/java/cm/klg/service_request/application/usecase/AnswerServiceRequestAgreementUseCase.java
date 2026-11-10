package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.service_request.FollowUpAnswer;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnswerServiceRequestAgreementUseCase {
  private final ServiceRequestRepository serviceRequestRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(Command command) {
    var serviceRequest = serviceRequestRepository.load(command.serviceRequestId());
    serviceRequest.answerAgreement(
        command.userId(), providerIdFor(command.userId()), command.answer());
    serviceRequestRepository.update(serviceRequest);
  }

  private ServiceProviderId providerIdFor(UserId userId) {
    try {
      return serviceProviderRepository.loadByUserId(userId).getId();
    } catch (ServiceProviderNotFoundException exception) {
      return ServiceProviderId.NULL;
    }
  }

  public record Command(ServiceRequestId serviceRequestId, UserId userId, FollowUpAnswer answer) {}
}
