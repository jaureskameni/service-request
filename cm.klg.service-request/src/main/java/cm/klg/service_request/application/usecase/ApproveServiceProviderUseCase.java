package cm.klg.service_request.application.usecase;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserNotFoundException;

public record ApproveServiceProviderUseCase(
    UserRepository userRepository, ServiceProviderRepository serviceProviderRepository) {

  public void execute(ApproveServiceProviderCommand command) {

    UserId userId = command.userId;
    if (!userRepository.existsById(userId)) {
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
      UserId userId, ServiceProviderId serviceProviderId, CreatedAt approvedAt) {}
}
