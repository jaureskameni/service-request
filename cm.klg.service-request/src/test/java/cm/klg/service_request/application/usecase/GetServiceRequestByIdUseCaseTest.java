package cm.klg.service_request.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetServiceRequestByIdUseCaseTest {

  @Mock private ServiceRequestRepository serviceRequestRepository;

  @InjectMocks private GetServiceRequestByIdUseCase objectUnderTest;

  @Test
  void shouldReturnServiceRequestWhenExists() {
    ServiceRequestId id = new ServiceRequestId(UUID.randomUUID());
    var serviceRequestView1 = mock(ServiceRequestView1.class);
    GetServiceRequestByIdUseCase.Command command = mock(GetServiceRequestByIdUseCase.Command.class);

    when(command.id()).thenReturn(id);
    when(serviceRequestRepository.loadByIdAsView1(id)).thenReturn(serviceRequestView1);

    ServiceRequestView1 resultUnderTest = objectUnderTest.execute(command);

    assertThat(resultUnderTest).isEqualTo(serviceRequestView1);
    verify(serviceRequestRepository).loadByIdAsView1(id);
  }
}
