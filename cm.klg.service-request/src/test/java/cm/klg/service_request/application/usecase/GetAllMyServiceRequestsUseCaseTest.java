package cm.klg.service_request.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.utils.PageData;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllMyServiceRequestsUseCaseTest {

  @Mock private ServiceRequestRepository serviceRequestRepository;
  @Mock private ServiceRequestView1 serviceRequestView1;

  @InjectMocks private GetAllMyServiceRequestsUseCase useCase;

  @Test
  void shouldLoadAllRequestsWhenStatusIsAbsent() {
    UserId userId = UserId.from(UUID.randomUUID());
    var command = new GetAllMyServiceRequestsUseCase.Command(userId, null, 20, 2);
    when(serviceRequestRepository.loadAllMyRequestsAsView1(
            eq(userId),
            argThat(pagination -> pagination.limit() == 20 && pagination.pageIndex() == 2)))
        .thenReturn(new PageData<>(1, List.of(serviceRequestView1)));

    var result = useCase.execute(command);

    assertThat(result.count()).isEqualTo(1);
    assertThat(result.serviceRequestView1s()).containsExactly(serviceRequestView1);
    verify(serviceRequestRepository)
        .loadAllMyRequestsAsView1(
            eq(userId),
            argThat(pagination -> pagination.limit() == 20 && pagination.pageIndex() == 2));
    verifyNoMoreInteractions(serviceRequestRepository);
  }

  @Test
  void shouldLoadRequestsByStatusWhenStatusIsProvided() {
    UserId userId = UserId.from(UUID.randomUUID());
    var command =
        new GetAllMyServiceRequestsUseCase.Command(userId, ServiceRequestStatus.ACCEPTED, 10, 0);
    when(serviceRequestRepository.loadAllMyRequestByStatusAsView1(
            eq(userId),
            eq(ServiceRequestStatus.ACCEPTED),
            argThat(pagination -> pagination.limit() == 10 && pagination.pageIndex() == 0)))
        .thenReturn(new PageData<>(1, List.of(serviceRequestView1)));

    var result = useCase.execute(command);

    assertThat(result.count()).isEqualTo(1);
    assertThat(result.serviceRequestView1s()).containsExactly(serviceRequestView1);
    verify(serviceRequestRepository)
        .loadAllMyRequestByStatusAsView1(
            eq(userId),
            eq(ServiceRequestStatus.ACCEPTED),
            argThat(pagination -> pagination.limit() == 10 && pagination.pageIndex() == 0));
    verifyNoMoreInteractions(serviceRequestRepository);
  }
}
