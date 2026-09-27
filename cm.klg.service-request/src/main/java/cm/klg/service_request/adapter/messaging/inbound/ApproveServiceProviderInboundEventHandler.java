package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

public record ApproveServiceProviderInboundEventHandler(
    ApproveServiceProviderUseCase approveServiceProviderUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderApprovedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderApprovedEventDTO> payloadType() {
    return ServiceProviderServiceProviderApprovedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderApprovedEventDTO> inboxEventCommand) {
    approveServiceProviderUseCase.execute(
        messagingInboundMapper.toApproveServiceProviderCommand(inboxEventCommand.data()));
  }
}
