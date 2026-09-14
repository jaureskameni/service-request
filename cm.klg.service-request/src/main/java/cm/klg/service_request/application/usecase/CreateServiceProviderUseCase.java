package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateServiceProviderUseCase {
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(Command command) {
    serviceProviderRepository.insertIfAbsent(
        ServiceProvider.reconstitute(
            command.serviceProviderId(), command.userId(), ServiceProviderStatus.PENDING, null));
  }

  public record Command(UserId userId, ServiceProviderId serviceProviderId) {}
}
