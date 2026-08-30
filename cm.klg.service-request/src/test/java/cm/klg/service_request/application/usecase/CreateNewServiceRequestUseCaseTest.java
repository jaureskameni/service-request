package cm.klg.service_request.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.service_request.ServiceTypeId;
import cm.klg.service_request.domain.user.IdentityId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateNewServiceRequestUseCaseTest {

  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private ServiceRequestRepository serviceRequestRepository;

  @Mock
  private cm.klg.service_request.application.outbound.DomainEventPublisher domainEventPublisher;

  @InjectMocks private CreateNewServiceRequestUseCase useCase;

  @Test
  void shouldCreateServiceRequestWithProvidedOptionalFieldsWhenProviderExists() {
    IdentityId userId = IdentityId.from(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    ServiceTypeId serviceTypeId = ServiceTypeId.from(UUID.randomUUID());
    var command =
        new CreateNewServiceRequestUseCase.Command(
            userId,
            providerId,
            serviceTypeId,
            ServiceRequestTitle.from("title"),
            ServiceRequestDescription.from("description"),
            ServiceRequestLocation.from("location"));
    when(serviceProviderRepository.existsById(providerId)).thenReturn(true);
    when(serviceProviderRepository.isApprovedById(providerId)).thenReturn(true);

    var result = useCase.execute(command);

    ArgumentCaptor<ServiceRequest> captor = ArgumentCaptor.forClass(ServiceRequest.class);
    verify(serviceRequestRepository).insert(captor.capture());
    assertThat(result).isEqualTo(captor.getValue().getId());
    assertThat(captor.getValue())
        .satisfies(
            serviceRequest -> {
              assertThat(serviceRequest.getUserId()).isEqualTo(userId);
              assertThat(serviceRequest.getServiceProviderId()).isEqualTo(providerId);
              assertThat(serviceRequest.getServiceTypeId()).isEqualTo(serviceTypeId);
              assertThat(serviceRequest.getTitle()).isNotNull();
              assertThat(serviceRequest.getTitle().value()).isEqualTo("title");
              assertThat(serviceRequest.getDescription()).isNotNull();
              assertThat(serviceRequest.getDescription().value()).isEqualTo("description");
              assertThat(serviceRequest.getLocation()).isNotNull();
              assertThat(serviceRequest.getLocation().value()).isEqualTo("location");
              assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatus.PENDING);
            });

    // verify event published
    verify(domainEventPublisher).publishServiceRequestCreatedEvent(any());
  }

  @Test
  void shouldCreateServiceRequestWithAbsentOptionalFieldsWhenProviderExists() {
    IdentityId userId = IdentityId.from(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    ServiceTypeId serviceTypeId = ServiceTypeId.from(UUID.randomUUID());
    var command =
        new CreateNewServiceRequestUseCase.Command(
            userId, providerId, serviceTypeId, null, null, null);
    when(serviceProviderRepository.existsById(providerId)).thenReturn(true);
    when(serviceProviderRepository.isApprovedById(providerId)).thenReturn(true);

    useCase.execute(command);

    ArgumentCaptor<ServiceRequest> captor = ArgumentCaptor.forClass(ServiceRequest.class);
    verify(serviceRequestRepository).insert(captor.capture());
    assertThat(captor.getValue().getTitle()).isNull();
    assertThat(captor.getValue().getDescription()).isNull();
    assertThat(captor.getValue().getLocation()).isNull();
  }

  @Test
  void shouldThrowWhenServiceProviderDoesNotExist() {
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    var command =
        new CreateNewServiceRequestUseCase.Command(
            IdentityId.from(UUID.randomUUID()),
            providerId,
            ServiceTypeId.from(UUID.randomUUID()),
            ServiceRequestTitle.from("title"),
            ServiceRequestDescription.from("description"),
            ServiceRequestLocation.from("location"));
    when(serviceProviderRepository.existsById(providerId)).thenReturn(false);

    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ServiceProviderNotFoundException.class);
    verifyNoInteractions(serviceRequestRepository);
  }
}
