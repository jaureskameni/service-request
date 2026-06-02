package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

public record ApproveServiceProviderInboundEventHandler(
    ApproveServiceProviderUseCase approveServiceProviderUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceProviderServiceProviderApprovedEventDTO> {
  @Override
  public String getEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderApprovedEventDTO> getDataType() {
    return ServiceProviderServiceProviderApprovedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceProviderServiceProviderApprovedEventDTO providerApprovedEventDTO,
      InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            approveServiceProviderUseCase.execute(
                messagingInboundMapper.toApproveServiceProviderCommand(providerApprovedEventDTO)));
  }
}
