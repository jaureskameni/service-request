package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.service_request.application.usecase.CreateUserUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record CreateUserInboundEventHandler(
    CreateUserUseCase createUserUseCase, MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<UamUserCreatedEventDTO> {
  @Override
  public String handledEventType() {
    return UamDomainEventType.USER_CREATED.getValue();
  }

  @Override
  public Class<UamUserCreatedEventDTO> payloadType() {
    return UamUserCreatedEventDTO.class;
  }

  @Override
  public void handle(InboxEventCommand<UamUserCreatedEventDTO> inboxEventCommand) {
    createUserUseCase.execute(messagingInboundMapper.toCreateUserCommand(inboxEventCommand.data()));
  }
}
