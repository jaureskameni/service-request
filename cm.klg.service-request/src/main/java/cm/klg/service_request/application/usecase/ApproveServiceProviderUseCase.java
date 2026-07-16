package cm.klg.service_request.application.usecase;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.IdentityId;
import cm.klg.service_request.domain.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApproveServiceProviderUseCase {
  private final UserRepository userRepository;
  private final ServiceProviderRepository serviceProviderRepository;

  public void execute(ApproveServiceProviderCommand command) {

    IdentityId userId = command.userId;
    if (!userRepository.existsByUserId(userId)) {
      throw new UserNotFoundException();
    }

    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            command.serviceProviderId(),
            userId,
            ServiceProviderStatus.APPROVED,
            command.approvedAt);

    serviceProviderRepository.insert(serviceProvider);
  }

  public record ApproveServiceProviderCommand(
      IdentityId userId, ServiceProviderId serviceProviderId, CreatedAt approvedAt) {}
}
