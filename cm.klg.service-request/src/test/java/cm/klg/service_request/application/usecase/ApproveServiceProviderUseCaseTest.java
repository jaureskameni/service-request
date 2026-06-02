package cm.klg.service_request.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserNotFoundException;
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

  @Mock private UserRepository userRepository;
  @Mock private ServiceProviderRepository serviceProviderRepository;

  @InjectMocks private ApproveServiceProviderUseCase useCase;

  @Test
  void shouldApproveServiceProviderWhenUserExists() {
    UserId userId = new UserId(UUID.randomUUID());
    ServiceProviderId serviceProviderId = new ServiceProviderId(UUID.randomUUID());
    CreatedAt approvedAt = CreatedAt.from(LocalDateTime.now());
    var command =
        new ApproveServiceProviderUseCase.ApproveServiceProviderCommand(
            userId, serviceProviderId, approvedAt);
    when(userRepository.existsById(userId)).thenReturn(true);

    useCase.execute(command);

    ArgumentCaptor<ServiceProvider> captor = ArgumentCaptor.forClass(ServiceProvider.class);
    verify(serviceProviderRepository).insert(captor.capture());
    assertThat(captor.getValue())
        .satisfies(
            serviceProvider -> {
              assertThat(serviceProvider.getId()).isEqualTo(serviceProviderId);
              assertThat(serviceProvider.getUserId()).isEqualTo(userId);
              assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
              assertThat(serviceProvider.getApprovedAt()).isEqualTo(approvedAt);
            });
  }

  @Test
  void shouldThrowWhenUserDoesNotExist() {
    UserId userId = new UserId(UUID.randomUUID());
    var command =
        new ApproveServiceProviderUseCase.ApproveServiceProviderCommand(
            userId, new ServiceProviderId(UUID.randomUUID()), CreatedAt.from(LocalDateTime.now()));
    when(userRepository.existsById(userId)).thenReturn(false);

    assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(UserNotFoundException.class);
    verifyNoInteractions(serviceProviderRepository);
  }
}
