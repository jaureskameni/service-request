package cm.klg.service_request.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetServiceRequestByIdUseCaseTest {

  @Mock private ServiceRequestRepository serviceRequestRepository;
  @Mock private ServiceProviderRepository serviceProviderRepository;

  @InjectMocks private GetServiceRequestByIdUseCase objectUnderTest;

  @Test
  void shouldReturnServiceRequestWhenExists() {
    ServiceRequestId id = new ServiceRequestId(UUID.randomUUID());
    UserId userId = UserId.from(UUID.randomUUID());
    var serviceRequest = mock(ServiceRequest.class);
    GetServiceRequestByIdUseCase.Command command = mock(GetServiceRequestByIdUseCase.Command.class);

    when(command.id()).thenReturn(id);
    when(command.userId()).thenReturn(userId);
    when(serviceRequest.isClient(userId)).thenReturn(true);
    when(serviceRequestRepository.load(id)).thenReturn(serviceRequest);

    ServiceRequest resultUnderTest = objectUnderTest.execute(command);

    assertThat(resultUnderTest).isEqualTo(serviceRequest);
    verify(serviceRequestRepository).load(id);
  }
}
