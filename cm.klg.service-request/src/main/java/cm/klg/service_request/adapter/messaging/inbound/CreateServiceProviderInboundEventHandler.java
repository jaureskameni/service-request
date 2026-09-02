package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_request.application.usecase.CreateServiceProviderUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

public record CreateServiceProviderInboundEventHandler(
    CreateServiceProviderUseCase createServiceProviderUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceProviderServiceProviderCreatedEventDTO> {

  @Override
  public String getEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderCreatedEventDTO> getDataType() {
    return ServiceProviderServiceProviderCreatedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceProviderServiceProviderCreatedEventDTO event, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createServiceProviderUseCase.execute(
                messagingInboundMapper.toCreateServiceProviderCommand(event)));
  }
}
