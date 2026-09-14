package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

@ExtendWith(MockitoExtension.class)
class ApproveServiceProviderInboundEventHandlerTest {

  @Mock private ApproveServiceProviderUseCase approveServiceProviderUseCase;
  @Mock private UseCaseExecutor useCaseExecutor;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private ApproveServiceProviderInboundEventHandler handler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(handler.getEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(handler.getDataType())
        .isEqualTo(ServiceProviderServiceProviderApprovedEventDTO.class);
  }

  @Test
  void shouldHandleServiceProviderApprovedEvent() {
    ServiceProviderServiceProviderApprovedEventDTO event =
        new ServiceProviderServiceProviderApprovedEventDTO();
    InboxEventCommand inboxEventCommand = mock(InboxEventCommand.class);
    ApproveServiceProviderUseCase.ApproveServiceProviderCommand command =
        new ApproveServiceProviderUseCase.ApproveServiceProviderCommand(
            UserId.from(UUID.randomUUID()),
            new ServiceProviderId(UUID.randomUUID()),
            CreatedAt.from(LocalDateTime.now()));

    when(messagingInboundMapper.toApproveServiceProviderCommand(event)).thenReturn(command);
    doAnswer(
            invocation -> {
              Runnable runnable = invocation.getArgument(0);
              runnable.run();
              return null;
            })
        .when(useCaseExecutor)
        .runCommand(any(Runnable.class));

    handler.handle(event, inboxEventCommand);

    verify(approveServiceProviderUseCase).execute(command);
  }
}
