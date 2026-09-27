package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.service_request.application.usecase.CreateServiceProviderUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

public record CreateServiceProviderInboundEventHandler(
    CreateServiceProviderUseCase createServiceProviderUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderCreatedEventDTO> {

  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderCreatedEventDTO> payloadType() {
    return ServiceProviderServiceProviderCreatedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderCreatedEventDTO> inboxEventCommand) {
    createServiceProviderUseCase.execute(
        messagingInboundMapper.toCreateServiceProviderCommand(inboxEventCommand.data()));
  }
}
