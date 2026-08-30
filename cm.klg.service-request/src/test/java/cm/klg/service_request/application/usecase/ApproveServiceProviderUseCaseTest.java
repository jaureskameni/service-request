package cm.klg.service_request.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.IdentityId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApproveServiceProviderUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;

  @InjectMocks private ApproveServiceProviderUseCase useCase;

  @Test
  void shouldApproveExistingServiceProvider() {
    IdentityId userId = IdentityId.from(UUID.randomUUID());
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    CreatedAt approvedAt = CreatedAt.from(LocalDateTime.now());
    var command =
        new ApproveServiceProviderUseCase.ApproveServiceProviderCommand(
            userId, serviceProviderId, approvedAt);

    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            serviceProviderId, userId, ServiceProviderStatus.PENDING, null);
    when(serviceProviderRepository.load(serviceProviderId)).thenReturn(serviceProvider);

    useCase.execute(command);

    ArgumentCaptor<ServiceProvider> captor = ArgumentCaptor.forClass(ServiceProvider.class);
    verify(serviceProviderRepository).update(captor.capture());
    assertThat(captor.getValue())
        .satisfies(
            sp -> {
              assertThat(sp.getId()).isEqualTo(serviceProviderId);
              assertThat(sp.getUserId()).isEqualTo(userId);
              assertThat(sp.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
              assertThat(sp.getApprovedAt()).isNotNull();
            });
  }
}
