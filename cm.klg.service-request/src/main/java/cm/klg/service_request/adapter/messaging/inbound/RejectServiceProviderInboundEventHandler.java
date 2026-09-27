package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.service_request.application.usecase.RejectServiceProviderUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

public record RejectServiceProviderInboundEventHandler(
    RejectServiceProviderUseCase rejectServiceProviderUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderRejectedEventDTO> {

  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderRejectedEventDTO> payloadType() {
    return ServiceProviderServiceProviderRejectedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderRejectedEventDTO> inboxEventCommand) {
    rejectServiceProviderUseCase.execute(
        messagingInboundMapper.toRejectServiceProviderCommand(inboxEventCommand.data()));
  }
}
