package cm.klg.service_request.application.usecase;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApproveServiceProviderUseCase {
  private final UserRepository userRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(ApproveServiceProviderCommand command) {
    ServiceProvider serviceProvider = serviceProviderRepository.load(command.serviceProviderId());
    serviceProvider.approve();
    serviceProviderRepository.update(serviceProvider);
  }

  public record ApproveServiceProviderCommand(
      UserId userId, ServiceProviderId serviceProviderId, CreatedAt approvedAt) {}
}
