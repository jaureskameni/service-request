package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.IdentityId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RejectServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(Command command) {
    var serviceProvider = serviceProviderRepository.load(command.serviceProviderId());
    serviceProvider.reject();
    serviceProviderRepository.update(serviceProvider);
  }

  public record Command(IdentityId userId, ServiceProviderId serviceProviderId) {}
}
