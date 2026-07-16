package cm.klg.service_request.application.usecase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.event.ServiceRequestCancelledEvent;
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
class CancelServiceRequestUseCaseTest {

  @Mock private ServiceRequestRepository serviceRequestRepository;
  @Mock private DomainEventPublisher domainEventPublisher;

  @InjectMocks private CancelServiceRequestUseCase objectUnderTest;

  @Test
  void shouldCancelServiceRequestWhenExistsAndUserMatches() {
    // Given
    var userId = IdentityId.from(UUID.randomUUID());
    var serviceRequest = mock(ServiceRequest.class);
    var event = mock(ServiceRequestCancelledEvent.class);
    ServiceRequestId requestId = ServiceRequestId.generate();

    when(serviceRequestRepository.load(requestId)).thenReturn(serviceRequest);
    when(serviceRequest.cancel(userId)).thenReturn(event);

    // When
    objectUnderTest.execute(new CancelServiceRequestUseCase.Command(userId, requestId));

    // Then
    verify(serviceRequest).cancel(userId);
    verify(serviceRequestRepository).update(serviceRequest);
    verify(domainEventPublisher).publishServiceRequestCancelledEvent(event);
  }
}
