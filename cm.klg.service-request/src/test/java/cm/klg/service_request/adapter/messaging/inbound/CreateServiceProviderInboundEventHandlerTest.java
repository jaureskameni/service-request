package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.usecase.CreateServiceProviderUseCase;
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
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

@ExtendWith(MockitoExtension.class)
class CreateServiceProviderInboundEventHandlerTest {

  @Mock private CreateServiceProviderUseCase createServiceProviderUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private CreateServiceProviderInboundEventHandler handler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(handler.handledEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue());
  }

  @Test
  void shouldReturnCorrectPayloadType() {
    assertThat(handler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderCreatedEventDTO.class);
  }

  @Test
  void shouldHandleServiceProviderCreatedEvent() {
    ServiceProviderServiceProviderCreatedEventDTO event =
        new ServiceProviderServiceProviderCreatedEventDTO();
    InboxEventCommand<ServiceProviderServiceProviderCreatedEventDTO> inboxEventCommand =
        mock(InboxEventCommand.class);
    CreateServiceProviderUseCase.Command command =
        new CreateServiceProviderUseCase.Command(
            UserId.from(UUID.randomUUID()), new ServiceProviderId(UUID.randomUUID()));

    when(inboxEventCommand.data()).thenReturn(event);
    when(messagingInboundMapper.toCreateServiceProviderCommand(event)).thenReturn(command);

    handler.handle(inboxEventCommand);

    verify(createServiceProviderUseCase).execute(command);
  }
}
