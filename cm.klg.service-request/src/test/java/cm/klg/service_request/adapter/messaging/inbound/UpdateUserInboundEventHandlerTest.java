package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.service_request.application.usecase.UpdateUserUseCase;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserInboundEventHandlerTest {

  @Mock private UpdateUserUseCase updateUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private UpdateUserInboundEventHandler updateUserInboundEventHandler;

  @Test
  void shouldReturnCorrectEventType() {
    assertThat(updateUserInboundEventHandler.getEventType())
        .isEqualTo(UamDomainEventType.USER_UPDATED.getValue());
  }

  @Test
  void shouldReturnCorrectDataType() {
    assertThat(updateUserInboundEventHandler.getDataType()).isEqualTo(UamUserUpdatedEventDTO.class);
  }

  @Test
  void shouldHandleUserUpdatedEvent() {
    UamUserUpdatedEventDTO userUpdatedEventDTO = new UamUserUpdatedEventDTO();
    InboxEventCommand inboxEventCommand = mock(InboxEventCommand.class);
    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            UserId.from(UUID.randomUUID()),
            Lastname.from("Doe"),
            Firstname.from("John"),
            EmailAddress.from("john@doe.com"),
            PhoneNumber.from("237", "699"));

    when(messagingInboundMapper.toUpdateUserCommand(userUpdatedEventDTO)).thenReturn(command);

    updateUserInboundEventHandler.handle(userUpdatedEventDTO, inboxEventCommand);

    verify(updateUserUseCase).execute(command);
  }
}
