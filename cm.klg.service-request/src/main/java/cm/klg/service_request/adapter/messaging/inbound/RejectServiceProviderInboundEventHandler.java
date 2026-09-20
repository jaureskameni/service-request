package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.service_request.application.usecase.RejectServiceProviderUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

public record RejectServiceProviderInboundEventHandler(
    RejectServiceProviderUseCase rejectServiceProviderUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceProviderServiceProviderRejectedEventDTO> {

  @Override
  public String getEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderRejectedEventDTO> getDataType() {
    return ServiceProviderServiceProviderRejectedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceProviderServiceProviderRejectedEventDTO event, InboxEventCommand inboxEventCommand) {
    rejectServiceProviderUseCase.execute(
        messagingInboundMapper.toRejectServiceProviderCommand(event));
  }
}
