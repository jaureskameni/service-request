package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.service_request.application.usecase.DeleteUserUseCase;
import cm.klg.service_request.domain.user.UserId;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.Objects;

public record DeleteUserInboundEventHandler(
    DeleteUserUseCase deleteUserUseCase, MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<UamUserDeletedEventDTO> {
  @Override
  public String getEventType() {
    return UamDomainEventType.USER_DELETED.getValue();
  }

  @Override
  public Class<UamUserDeletedEventDTO> getDataType() {
    return UamUserDeletedEventDTO.class;
  }

  @Override
  public void handle(
      UamUserDeletedEventDTO userDeletedEventDTO, InboxEventCommand inboxEventCommand) {
    deleteUserUseCase.execute(UserId.from(Objects.requireNonNull(userDeletedEventDTO.getId())));
  }
}
