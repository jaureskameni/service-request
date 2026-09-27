package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.usecase.RejectServiceProviderUseCase;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

@ExtendWith(MockitoExtension.class)
class RejectServiceProviderInboundEventHandlerTest {

  @Mock private RejectServiceProviderUseCase rejectServiceProviderUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private RejectServiceProviderInboundEventHandler handler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(handler.handledEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue());
  }

  @Test
  void shouldReturnCorrectPayloadType() {
    assertThat(handler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderRejectedEventDTO.class);
  }

  @Test
  void shouldHandleServiceProviderRejectedEvent() {
    ServiceProviderServiceProviderRejectedEventDTO event =
        new ServiceProviderServiceProviderRejectedEventDTO();
    InboxEventCommand<ServiceProviderServiceProviderRejectedEventDTO> inboxEventCommand =
        mock(InboxEventCommand.class);
    RejectServiceProviderUseCase.Command command =
        new RejectServiceProviderUseCase.Command(
            UserId.from(UUID.randomUUID()), new ServiceProviderId(UUID.randomUUID()));

    when(inboxEventCommand.data()).thenReturn(event);
    when(messagingInboundMapper.toRejectServiceProviderCommand(event)).thenReturn(command);

    handler.handle(inboxEventCommand);

    verify(rejectServiceProviderUseCase).execute(command);
  }
}
