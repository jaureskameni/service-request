package cm.klg.service_request.application.usecase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.IdentityId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptServiceRequestUseCaseTest {

  @Mock private ServiceRequestRepository serviceRequestRepository;
  @Mock private ServiceProviderRepository serviceProviderRepository;
  @Mock private DomainEventPublisher domainEventPublisher;

  @InjectMocks private AcceptServiceRequestUseCase objectUnderTest;

  @Test
  void shouldAcceptServiceRequestWhenExistsAndProviderMatches() {
    // Given
    var userId = IdentityId.from(UUID.randomUUID());
    var providerId = ServiceProviderId.generate();
    var provider = mock(ServiceProvider.class);
    var serviceRequest = mock(ServiceRequest.class);
    var event = mock(ServiceRequestAcceptedEvent.class);
    ServiceRequestId requestId = ServiceRequestId.generate();

    when(serviceProviderRepository.loadByUserId(userId)).thenReturn(provider);
    when(serviceProviderRepository.isApprovedByUserId(userId)).thenReturn(true);
    when(serviceRequestRepository.load(requestId)).thenReturn(serviceRequest);
    when(provider.getId()).thenReturn(providerId);
    when(serviceRequest.accept(providerId)).thenReturn(event);

    // When
    objectUnderTest.execute(new AcceptServiceRequestUseCase.Command(userId, requestId));

    // Then
    verify(serviceRequest).accept(providerId);
    verify(serviceRequestRepository).update(serviceRequest);
    verify(domainEventPublisher).publishServiceRequestAcceptedEvent(event);
  }
}
