package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.service_request.application.usecase.DeleteUserUseCase;
import cm.klg.service_request.domain.user.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserInboundEventHandlerTest {

  @Mock private DeleteUserUseCase deleteUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private DeleteUserInboundEventHandler deleteUserInboundEventHandler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(deleteUserInboundEventHandler.getEventType())
        .isEqualTo(UamDomainEventType.USER_DELETED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(deleteUserInboundEventHandler.getDataType()).isEqualTo(UamUserDeletedEventDTO.class);
  }

  @Test
  void shouldHandleUserDeletedEvent() {
    UUID userId = UUID.randomUUID();
    UamUserDeletedEventDTO userDeletedEventDTO = new UamUserDeletedEventDTO().id(userId);
    InboxEventCommand inboxEventCommand = mock(InboxEventCommand.class);

    deleteUserInboundEventHandler.handle(userDeletedEventDTO, inboxEventCommand);

    verify(deleteUserUseCase).execute(UserId.from(userId));
  }
}
